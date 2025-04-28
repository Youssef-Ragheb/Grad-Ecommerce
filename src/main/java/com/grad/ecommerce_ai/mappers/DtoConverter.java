package com.grad.ecommerce_ai.mappers;

import com.grad.ecommerce_ai.dto.*;
import com.grad.ecommerce_ai.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class DtoConverter {


    public static List<BranchDTO> branchListToDtoList(List<Branch> branchList) {
        return branchList.stream().map(DtoConverter::branchToDto).collect(Collectors.toList());
    }

    public static List<CompanyDTO> companyListToDtoList(List<Company> companyList) {
        return companyList.stream().map(DtoConverter::companyToDto).collect(Collectors.toList());
    }

    public static BranchDTO branchToDto(Branch branch) {
        BranchDTO dto = new BranchDTO();
        dto.setBranchId(branch.getBranchId());
        dto.setBranchName(branch.getBranchName());
        dto.setAddress(branch.getAddress());
        dto.setCity(branch.getCity());
        dto.setPhone(branch.getPhone());
        dto.setEmail(branch.getEmail());
        dto.setBranchState(branch.getBranchState());
        dto.setZip(branch.getZip());
        dto.setLat(branch.getLat());
        dto.setLng(branch.getLng());
        if (branch.getCompany() != null) {
            dto.setCompanyLogoURl(branch.getCompany().getLogoUrl());
        }
        return dto;
    }

    public static Branch branchDTOToBranch(BranchDTO dto) {
        Branch branch = new Branch();

        branch.setBranchId(dto.getBranchId());
        branch.setBranchName(dto.getBranchName());
        branch.setAddress(dto.getAddress());
        branch.setCity(dto.getCity());
        branch.setPhone(dto.getPhone());
        branch.setEmail(dto.getEmail());
        branch.setBranchState(dto.getBranchState());
        branch.setZip(dto.getZip());
        branch.setLat(dto.getLat());
        branch.setLng(dto.getLng());
        return branch;
    }

    public static CategoryDTO categoryToDto(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getId());
        categoryDTO.setCategoryName(category.getCategoryName());
        categoryDTO.setLogoUrl(categoryDTO.getLogoUrl());
        return categoryDTO;
    }

    public static Category categoryDtoToCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getCategoryId());
        category.setCategoryName(categoryDTO.getCategoryName());
        category.setLogo(categoryDTO.getLogoUrl());
        return category;
    }

    public static ActiveIngredientDTO activeIngredientToDto(ActiveIngredient activeIngredient) {
        ActiveIngredientDTO activeIngredientDTO = new ActiveIngredientDTO();
        activeIngredientDTO.setId(activeIngredient.getId());
        activeIngredientDTO.setActiveIngredient(activeIngredient.getActiveIngredientName());
        activeIngredientDTO.setIngredientArabicName(activeIngredient.getIngredientArabicName());
        activeIngredientDTO.setDescription(activeIngredient.getDescription());
        return activeIngredientDTO;
    }

    public static ActiveIngredient activeIngredientDtoToActiveIngredient(ActiveIngredientDTO activeIngredientDTO) {
        ActiveIngredient activeIngredient = new ActiveIngredient();
        activeIngredient.setId(activeIngredientDTO.getId());
        activeIngredient.setActiveIngredientName(activeIngredientDTO.getActiveIngredient());
        activeIngredient.setIngredientArabicName(activeIngredientDTO.getIngredientArabicName());
        activeIngredient.setDescription(activeIngredientDTO.getDescription());
        return activeIngredient;
    }

    public static DrugDTO drugToDto(Drugs drugs) {
        DrugDTO dto = new DrugDTO();
        dto.setDrugId(drugs.getId());
        dto.setDescription(drugs.getDescription());
        dto.setCategoryDTO(categoryToDto(drugs.getCategory()));
        dto.setActiveIngredientDto(activeIngredientToDto(drugs.getActiveIngredient()));
        dto.setDrugName(drugs.getDrugName());
        dto.setLogoUrl(drugs.getLogo());
        return dto;
    }

    public static Drugs drugDtoToDrug(DrugDTO dto) {
        Drugs drugs = new Drugs();
        drugs.setDrugName(dto.getDrugName());
        drugs.setLogo(dto.getLogoUrl());
        drugs.setId(dto.getDrugId());
        drugs.setDescription(dto.getDescription());
        drugs.setActiveIngredient(activeIngredientDtoToActiveIngredient(dto.getActiveIngredientDto()));
        drugs.setCategory(categoryDtoToCategory(dto.getCategoryDTO()));
        return drugs;
    }

    public static OrderDTO orderToDto(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setTotalPrice(order.getTotalPrice());
        List<RequestDTO> requestDTOS = new ArrayList<>();
        for (Request request : order.getRequests()) {
            requestDTOS.add(RequestToRequestDTO(request));
        }
        orderDTO.setRequests(requestDTOS);
        orderDTO.setStatus(order.getStatus());
        orderDTO.setPaymentMethod(order.getPaymentMethod());
        orderDTO.setUser(order.getUser());
        orderDTO.setCreatedAt(order.getCreatedAt());
        return orderDTO;
    }

    public static Order orderDtoToOrder(OrderDTO order) {
        Order orderDTO = new Order();
        orderDTO.setId(order.getId());
        orderDTO.setTotalPrice(order.getTotalPrice());
        List<Request> list = new ArrayList<>();
        for (RequestDTO requestDTO : order.getRequests()) {
            list.add(RequestDtoToRequest(requestDTO));
        }
        orderDTO.setRequests(list);
        orderDTO.setStatus(order.getStatus());
        orderDTO.setPaymentMethod(order.getPaymentMethod());
        orderDTO.setUser(order.getUser());
        orderDTO.setCreatedAt(order.getCreatedAt());
        return orderDTO;
    }

    public static CustomerInfoDTO UserToCustomerInfoDTO(User user) {
        CustomerInfoDTO customerInfoDTO = new CustomerInfoDTO();
        customerInfoDTO.setAddress(user.getAddress());
        customerInfoDTO.setCity(user.getCity());
        customerInfoDTO.setPhone(user.getPhone());
        customerInfoDTO.setLastName(user.getLastName());
        customerInfoDTO.setFirstName(user.getFirstName());
        return customerInfoDTO;
    }

    public static ItemDTO toItemDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setDrugDTO(drugToDto(item.getDrug()));
        dto.setUnitPrice(item.getPrice());
        dto.setQuantity(item.getQuantity());
        return dto;
    }

    public static Item toItemEntity(ItemDTO dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setDrug(drugDtoToDrug(dto.getDrugDTO()));
        item.setPrice(dto.getUnitPrice());
        item.setQuantity(dto.getQuantity());
        return item;
    }

    public static OrderDTO orderToDtoWithOutRequest(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setRequests(new ArrayList<>());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setPaymentMethod(order.getPaymentMethod());
        orderDTO.setUser(order.getUser());
        orderDTO.setCreatedAt(order.getCreatedAt());
        return orderDTO;
    }

    public static Order orderDtoToOrderWithOutRequest(OrderDTO orderDTO) {
        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setRequests(new ArrayList<>());
        order.setStatus(orderDTO.getStatus());
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setUser(orderDTO.getUser());
        order.setCreatedAt(orderDTO.getCreatedAt());
        return order;
    }

    public static RequestDTO RequestToRequestDTO(Request request) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setRequestId(request.getRequestId());
        requestDTO.setStatus(request.getStatus());
        requestDTO.setCustomer(UserToCustomerInfoDTO(request.getCustomer()));
        List<ItemDTO> list = new ArrayList<>();
        for (Item item : request.getItems()) {
            list.add(toItemDTO(item));
        }
        requestDTO.setItems(list);
        requestDTO.setOrder(orderToDtoWithOutRequest(request.getOrder()));
        requestDTO.setBranch(branchToDto(request.getBranch()));
        requestDTO.setTotalPriceOfRequest(request.getTotalPriceOfRequest());

        return requestDTO;
    }

    public static Request RequestDtoToRequest(RequestDTO requestDTO) {
        Request request = new Request();
        request.setRequestId(requestDTO.getRequestId());
        request.setStatus(requestDTO.getStatus());
        request.setOrder(orderDtoToOrderWithOutRequest(requestDTO.getOrder()));
        request.setCustomer(requestDTO.getOrder().getUser());
        List<Item> list = new ArrayList<>();
        for (ItemDTO item : requestDTO.getItems()) {
            list.add(toItemEntity(item));
        }
        request.setItems(list);
        request.setBranch(branchDTOToBranch(requestDTO.getBranch()));
        request.setTotalPriceOfRequest(requestDTO.getTotalPriceOfRequest());
        return request;
    }

//    public static BranchDTO branchToBranchWithCompanyDto(Branch branch) {
//        BranchDTO dto = branchToDto(branch);
//
//        if (branch.getCompany() != null) {
//            dto.setCompanyDto(companyToDto(branch.getCompany()));
//        }
//
//        return dto;
//    }
//
//    public static Company dtoToCompany(CompanyDTO companyDto) {
//        Company company = new Company();
//
//        company.setCompanyId(companyDto.getCompanyId());
//        company.setName(companyDto.getName());
//        company.setCompanyEmail(companyDto.getCompanyEmail());
//        company.setPhone(companyDto.getPhone());
//        company.setLogoUrl(companyDto.getLogoUrl());
//
//        return company;
//    }

    public static CompanyDTO companyToDto(Company company) {
        CompanyDTO dto = new CompanyDTO();

        dto.setCompanyId(company.getCompanyId());
        dto.setName(company.getName());
        dto.setCompanyEmail(company.getCompanyEmail());
        dto.setPhone(company.getPhone());
        dto.setLogoUrl(company.getLogoUrl());

        return dto;
    }

//    public static CompanyDTO companyToCompanyWithBranchesDto(Company company) {
//        CompanyDTO dto = companyToDto(company);
//
//        if (company.getBranchList() != null) {
//            dto.setBranchList(branchListToDtoList(company.getBranchList()));
//        }
//
//        return dto;
//    }
}



