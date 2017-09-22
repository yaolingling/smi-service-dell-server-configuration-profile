package com.dell.isg.smi.service.server.configuration.manager;

import com.dell.isg.smi.service.server.configuration.model.Attribute;
import com.dell.isg.smi.service.server.configuration.model.ComponentList;
import com.dell.isg.smi.service.server.configuration.model.NestedComponent;
import com.dell.isg.smi.service.server.configuration.model.ServerAndNetworkShareRequest;
import com.dell.isg.smi.service.server.configuration.model.ServerComponent;
import com.dell.isg.smi.service.server.configuration.model.SubComponent;
import com.dell.isg.smi.service.server.configuration.model.SystemConfiguration;
import org.apache.catalina.Server;
import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.bind.Binder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ConfigurationManagerImplTest {
    private IConfigurationManager configManager;

    @Before
    public void setup() {
        configManager = new ConfigurationManagerImpl();
    }

    @Test
    public void testForceUpdateComponents() throws Exception {
        URL origFile = Thread.currentThread().getContextClassLoader().getResource("exported-scp-1.xml");
        assertNotNull("Failed to load exported-scp-1.xml", origFile);

        Path tempFile = Files.createTempFile("ConfigurationManagerImplTest", "testIt");
        try {
            Files.copy(new File(origFile.getPath()).toPath(), tempFile, REPLACE_EXISTING);

            ComponentList request = new ComponentList();
            ServerAndNetworkShareRequest shareRequest = new ServerAndNetworkShareRequest();
            shareRequest.setFilePathName(tempFile.toString());
            request.setServerAndNetworkShareRequest(shareRequest);
            List<ServerComponent> serverComponents = new ArrayList<>();

            ServerComponent component = new ServerComponent();
            component.setFQDD("BIOS.Setup.1-1");
            component.setAttributes(new ArrayList<>());
            Attribute attr = new Attribute();
            attr.setName("MemTest");
            attr.setValue("Enabled");
            component.getAttributes().add(attr);
            attr = new Attribute();
            attr.setName("SecureBoot");
            attr.setValue("Enabled");
            component.getAttributes().add(attr);
            serverComponents.add(component);

            component = new ServerComponent();
            component.setFQDD("RAID.Slot.1-1");
            component.setSubComponents(new ArrayList<>());

            SubComponent subComponent = new SubComponent();
            subComponent.setAttributes(new ArrayList<>());
            subComponent.setFQDD("Enclosure.Internal.0-0:RAID.Slot.1-1");
            subComponent.setNestedComponents(new ArrayList<>());
            NestedComponent nestedComponent = new NestedComponent();
            nestedComponent.setFQDD("Disk.Bay.0:Enclosure.Internal.0-0:RAID.Slot.1-1");
            nestedComponent.setAttributes(new ArrayList<>());
            attr = new Attribute();
            attr.setName("RAIDHotSpareStatus");
            attr.setValue("Yes");
            nestedComponent.getAttributes().add(attr);
            attr = new Attribute();
            attr.setName("RAIDPDState");
            attr.setValue("Online");
            nestedComponent.getAttributes().add(attr);
            subComponent.getNestedComponents().add(nestedComponent);
            component.getSubComponents().add(subComponent);

            subComponent = new SubComponent();
            subComponent.setAttributes(new ArrayList<>());
            subComponent.setFQDD("Disk.Virtual.0:RAID.Slot.1-1");
            attr = new Attribute();
            attr.setName("T10PIStatus");
            attr.setValue("Enabled");
            subComponent.getAttributes().add(attr);
            attr = new Attribute();
            attr.setName("DiskCachePolicy");
            attr.setValue("Enabled");
            subComponent.getAttributes().add(attr);
            component.getSubComponents().add(subComponent);
            serverComponents.add(component);
            request.setServerComponents(serverComponents);

            List<ServerComponent> updatedComponents = configManager.updateComponents(request, true);

            assertTrue(updatedComponents.size() == 2);
            for (ServerComponent serverComponent : updatedComponents) {
                if (CollectionUtils.isNotEmpty(serverComponent.getAttributes()))
                {
                    assertTrue(serverComponent.getAttributes().size() == 2);
                }
                if (CollectionUtils.isNotEmpty(serverComponent.getSubComponents())) {
                    for (SubComponent sub : serverComponent.getSubComponents()) {
                        if (CollectionUtils.isNotEmpty(sub.getAttributes()))
                        {
                            assertTrue(sub.getAttributes().size() == 2);
                        }
                        if (CollectionUtils.isNotEmpty(sub.getNestedComponents())) {
                            for (NestedComponent nested : sub.getNestedComponents()) {
                                if (CollectionUtils.isNotEmpty(nested.getAttributes()))
                                {
                                    assertTrue(nested.getAttributes().size() == 2);
                                }
                            }
                        }
                    }
                }
            }

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document document = db.parse(tempFile.toFile());
            JAXBContext jaxbContext = JAXBContextFactory.createContext(new Class[] { SystemConfiguration.class },
                                                                       null);

            Binder<Node> binder = jaxbContext.createBinder();
            binder.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            binder.setProperty(Marshaller.JAXB_FRAGMENT, true);

            SystemConfiguration systemConfig = (SystemConfiguration) binder.unmarshal(document);
            assertNotNull(systemConfig);
            for (ServerComponent serverComponent : systemConfig.getServerComponents()) {
                if (serverComponent.getFQDD().equals("BIOS.Setup.1-1")) {
                    for (Attribute attribute : serverComponent.getAttributes()) {
                        switch (attribute.getName()) {
                            case "MemTest":
                                assertTrue(attribute.getValue().equals("Enabled"));
                                break;
                            case "SecureBoot":
                                assertTrue(attribute.getValue().equals("Enabled"));
                                break;
                            default:
                                break;
                        }
                    }
                } else if (serverComponent.getFQDD().equals("RAID.Slot.1-1")) {
                    for (SubComponent sub : serverComponent.getSubComponents())
                    {
                        if (sub.getFQDD().equals("Disk.Virtual.0:RAID.Slot.1-1"))
                        {
                            for (Attribute attribute : sub.getAttributes()) {
                                switch (attribute.getName())
                                {
                                    case "T10PIStatus":
                                        assertTrue(attribute.getValue().equals("Enabled"));
                                        break;
                                    case "DiskCachePolicy":
                                        assertTrue(attribute.getValue().equals("Enabled"));
                                        break;
                                }
                            }
                        } else if (sub.getFQDD().equals("Enclosure.Internal.0-0:RAID.Slot.1-1"))
                        {
                            for (NestedComponent nested : subComponent.getNestedComponents())
                            {
                                if (nested.getFQDD().equals("Disk.Bay.0:Enclosure.Internal.0-0:RAID.Slot.1-1"))
                                {
                                    for (Attribute attribute : nestedComponent.getAttributes())
                                    {
                                        if (attribute.getName().equals("RAIDHotSpareStatus"))
                                        {
                                            assertTrue(attribute.getValue().equals("Yes"));
                                        }
                                        else if (attribute.getName().equals(("RAIDPDState")))
                                        {
                                            assertTrue(attribute.getValue().equals("Online"));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    public void testUpdateComponents() throws Exception {
        URL origFile = Thread.currentThread().getContextClassLoader().getResource("exported-scp-1.xml");
        assertNotNull("Failed to load exported-scp-1.xml", origFile);

        Path tempFile = Files.createTempFile("ConfigurationManagerImplTest", "testIt");
        try {
            Files.copy(new File(origFile.getPath()).toPath(), tempFile, REPLACE_EXISTING);

            ComponentList request = new ComponentList();
            ServerAndNetworkShareRequest shareRequest = new ServerAndNetworkShareRequest();
            shareRequest.setFilePathName(tempFile.toString());
            request.setServerAndNetworkShareRequest(shareRequest);
            List<ServerComponent> serverComponents = new ArrayList<>();

            ServerComponent component = new ServerComponent();
            component.setFQDD("BIOS.Setup.1-1");
            component.setAttributes(new ArrayList<>());
            Attribute attr = new Attribute();
            attr.setName("MemTest");
            attr.setValue("Enabled");
            component.getAttributes().add(attr);
            attr = new Attribute();
            attr.setName("SecureBoot");
            attr.setValue("Enabled");
            component.getAttributes().add(attr);
            serverComponents.add(component);

            component = new ServerComponent();
            component.setFQDD("RAID.Slot.1-1");
            component.setSubComponents(new ArrayList<>());

            SubComponent subComponent = new SubComponent();
            subComponent.setAttributes(new ArrayList<>());
            subComponent.setFQDD("Enclosure.Internal.0-0:RAID.Slot.1-1");
            subComponent.setNestedComponents(new ArrayList<>());
            NestedComponent nestedComponent = new NestedComponent();
            nestedComponent.setFQDD("Disk.Bay.0:Enclosure.Internal.0-0:RAID.Slot.1-1");
            nestedComponent.setAttributes(new ArrayList<>());
            attr = new Attribute();
            attr.setName("RAIDHotSpareStatus");
            attr.setValue("Yes");
            nestedComponent.getAttributes().add(attr);
            attr = new Attribute();
            attr.setName("RAIDPDState");
            attr.setValue("Online");
            nestedComponent.getAttributes().add(attr);
            subComponent.getNestedComponents().add(nestedComponent);
            component.getSubComponents().add(subComponent);

            subComponent = new SubComponent();
            subComponent.setAttributes(new ArrayList<>());
            subComponent.setFQDD("Disk.Virtual.0:RAID.Slot.1-1");
            attr = new Attribute();
            attr.setName("T10PIStatus");
            attr.setValue("Enabled");
            subComponent.getAttributes().add(attr);
            attr = new Attribute();
            attr.setName("DiskCachePolicy");
            attr.setValue("Enabled");
            subComponent.getAttributes().add(attr);
            component.getSubComponents().add(subComponent);
            serverComponents.add(component);
            request.setServerComponents(serverComponents);

            List<ServerComponent> updatedComponents = configManager.updateComponents(request, false);

            assertTrue(updatedComponents.size() == 2);
            for (ServerComponent serverComponent : updatedComponents) {
                if (CollectionUtils.isNotEmpty(serverComponent.getAttributes()))
                {
                    assertTrue(serverComponent.getAttributes().size() == 1);
                }
                if (CollectionUtils.isNotEmpty(serverComponent.getSubComponents())) {
                    for (SubComponent sub : serverComponent.getSubComponents()) {
                        if (CollectionUtils.isNotEmpty(sub.getAttributes()))
                        {
                            assertTrue(sub.getAttributes().size() == 1);
                        }
                        if (CollectionUtils.isNotEmpty(sub.getNestedComponents())) {
                            for (NestedComponent nested : sub.getNestedComponents()) {
                                if (CollectionUtils.isNotEmpty(nested.getAttributes()))
                                {
                                    assertTrue(nested.getAttributes().size() == 1);
                                }
                            }
                        }
                    }
                }
            }

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document document = db.parse(tempFile.toFile());
            JAXBContext jaxbContext = JAXBContextFactory.createContext(new Class[] { SystemConfiguration.class },
                                                                       null);

            Binder<Node> binder = jaxbContext.createBinder();
            binder.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            binder.setProperty(Marshaller.JAXB_FRAGMENT, true);

            SystemConfiguration systemConfig = (SystemConfiguration) binder.unmarshal(document);
            assertNotNull(systemConfig);
            for (ServerComponent serverComponent : systemConfig.getServerComponents()) {
                if (serverComponent.getFQDD().equals("BIOS.Setup.1-1")) {
                    for (Attribute attribute : serverComponent.getAttributes()) {
                        switch (attribute.getName()) {
                            case "MemTest":
                                assertTrue(attribute.getValue().equals("Enabled"));
                                break;
                            case "SecureBoot":
                                fail("Attribute SecureBoot should be commented out!");
                                break;
                            default:
                                break;
                        }
                    }
                } else if (serverComponent.getFQDD().equals("RAID.Slot.1-1")) {
                    for (SubComponent sub : serverComponent.getSubComponents())
                    {
                        if (sub.getFQDD().equals("Disk.Virtual.0:RAID.Slot.1-1"))
                        {
                            for (Attribute attribute : sub.getAttributes()) {
                                switch (attribute.getName())
                                {
                                    case "T10PIStatus":
                                        fail("Attribute T10PIStatus should be commented out!");
                                        break;
                                    case "DiskCachePolicy":
                                        assertTrue(attribute.getValue().equals("Enabled"));
                                        break;
                                }
                            }
                        } else if (sub.getFQDD().equals("Enclosure.Internal.0-0:RAID.Slot.1-1"))
                        {
                            for (NestedComponent nested : subComponent.getNestedComponents())
                            {
                                if (nested.getFQDD().equals("Disk.Bay.0:Enclosure.Internal.0-0:RAID.Slot.1-1"))
                                {
                                    for (Attribute attribute : nestedComponent.getAttributes())
                                    {
                                        switch (attribute.getName())
                                        {
                                            case "RAIDHotSpareStatus":
                                                fail("Attribute RAIDHotSpareStatus should be commented out!");
                                                break;
                                            case "RAIDPDState":
                                                assertTrue(attribute.getValue().equals("Online"));
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }


}
