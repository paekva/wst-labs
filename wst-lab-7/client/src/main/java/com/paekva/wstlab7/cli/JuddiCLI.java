package com.paekva.wstlab7.cli;

import com.paekva.wstlab7.client.service.SQLException_Exception;
import com.paekva.wstlab7.client.service.Students;
import com.paekva.wstlab7.juddi.JUDDIUtil;
import lombok.SneakyThrows;
import org.apache.juddi.api_v3.AccessPointType;
import org.uddi.api_v3.*;

import javax.xml.ws.BindingProvider;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static com.paekva.wstlab7.Client.*;

public class JuddiCLI {
    public static void juddiLoop(final BufferedReader reader) throws IOException, SQLException_Exception {
        JUDDICommand command;
        int currentState = 0;
        writeHelp();
        while (true) {
            currentState = readState(currentState, reader);
            if (currentState < 0 || currentState > JUDDICommand.values().length) {
                continue;
            } else if (currentState == 0) {
                writeHelp();
                continue;
            }
            command = JUDDICommand.values()[currentState - 1];
            switch (command) {
                case LIST_BUSINESS:
                    listBusinesses();
                    break;
                case CREATE_BUSINESS:
                    System.out.println("Введите имя бизнеса");
                    String bn = readString(reader);
                    if (bn != null) {
                        createBusiness(bn);
                    }
                    break;
                case CREATE_SERVICE:
                    listBusinesses();
                    String bk;
                    do {
                        System.out.println("Введите ключ бизнеса");
                        bk = readString(reader);
                    } while (bk == null);

                    String sn;
                    do {
                        System.out.println("Введите имя сервиса");
                        sn = readString(reader);
                    } while (sn == null);

                    String surl;
                    do {
                        System.out.println("Введите ссылку на wsdl");
                        surl = readString(reader);
                    } while (surl == null);
                    createService(bk, sn, surl);

                    break;
                case FIND_SERVICE:
                    System.out.println("Введите имя сервиса для поиска");
                    String fsn = readString(reader);
                    filterServices(fsn);
                    break;
                case USE_SERVICE:
                    System.out.println("Введите ключ сервиса");
                    String key = readString(reader);
                    if (key != null) {
                        if (useService(key)) {
                            commandMode = CommandMode.SERVICE;
                            StudentCLI.studentLoop(reader);
                        }
                    }
                    break;
                case QUIT:
                    System.out.println("До свидания!");
                    System.exit(0);
            }
        }
    }

    @SneakyThrows
    private static void listBusinesses() {
        JUDDIUtil.printBusinessInfo(juddiClient.getBusinessList().getBusinessInfos());
    }


    @SneakyThrows
    public static void createBusiness(String businessName) {
        businessName = businessName.trim();
        BusinessDetail business = juddiClient.createBusiness(businessName);
        System.out.println("New business was created");
        for (BusinessEntity businessEntity : business.getBusinessEntity()) {
            System.out.printf("Key: '%s'\n", businessEntity.getBusinessKey());
            System.out.printf(
                    "Name: '%s'\n",
                    businessEntity.getName().stream().map(Name::getValue).collect(Collectors.joining(" ")));
        }
    }

    @SneakyThrows
    private static void createService(String businessKey, String serviceName, String wsdlUrl) {
        List<ServiceDetail> serviceDetails = juddiClient
                .publishUrl(businessKey.trim(), serviceName.trim(), wsdlUrl.trim());
        System.out.printf("Services published from wsdl %s\n", wsdlUrl);
        JUDDIUtil.printServicesInfo(serviceDetails.stream()
                .map(ServiceDetail::getBusinessService)
                .flatMap(List::stream)
                .collect(Collectors.toList())
        );
    }

    @SneakyThrows
    private static void filterServices(String filterArg) {
        List<BusinessService> services = juddiClient.getServices(filterArg);
        JUDDIUtil.printServicesInfo(services);
    }

    @SneakyThrows
    private static boolean useService(String serviceKey) {

        ServiceDetail serviceDetail = juddiClient.getService(serviceKey.trim());
        if (serviceDetail == null || serviceDetail.getBusinessService() == null || serviceDetail
                .getBusinessService().isEmpty()) {
            System.out.printf("Can not find service by key '%s'\b", serviceKey);
            return false;
        }
        List<BusinessService> services = serviceDetail.getBusinessService();
        BusinessService businessService = services.get(0);
        BindingTemplates bindingTemplates = businessService.getBindingTemplates();
        if (bindingTemplates == null || bindingTemplates.getBindingTemplate().isEmpty()) {
            System.out.printf("No binding template found for service '%s' '%s'\n",
                    serviceKey,
                    businessService.getBusinessKey());
            return false;
        }
        for (BindingTemplate bindingTemplate : bindingTemplates.getBindingTemplate()) {
            AccessPoint accessPoint = bindingTemplate.getAccessPoint();
            if (accessPoint.getUseType().equals(AccessPointType.END_POINT.toString())) {
                String value = accessPoint.getValue();
                System.out.printf("Using endpoint '%s'\n", value);
                changeEndpointUrl(value);
                return true;
            }
        }
        System.out.printf("No endpoint found for service '%s'\n", serviceKey);
        return false;
    }

    @SneakyThrows
    public static void changeEndpointUrl(String endpointUrl) {
        URL url = new URL(endpointUrl.trim());

        Students studentsService = new Students(url);
        studentsServicePort = studentsService.getStudentsServicePort();
        ((BindingProvider) studentsServicePort).getRequestContext()
                .put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointUrl.trim());
    }
}
