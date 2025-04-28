package com.grad.ecommerce_ai.mappers;

import com.grad.ecommerce_ai.dto.InventoryDrugDTO;
import com.grad.ecommerce_ai.entity.InventoryDrug;

import static com.grad.ecommerce_ai.mappers.DtoConverter.*;


public class InventoryDrugMapper {


    public static InventoryDrug inventoryDrugDtoToInventoryDrug(InventoryDrugDTO dto) {
        if (dto == null) {
            return null;
        }

        InventoryDrug inventoryDrug = new InventoryDrug();

        inventoryDrug.setId(dto.getId());
        inventoryDrug.setDrug(drugDtoToDrug(dto.getDrugDTO()));
        inventoryDrug.setPrice(dto.getPrice());
        inventoryDrug.setStock(dto.getStock());
        inventoryDrug.setBranch(branchDTOToBranch(dto.getBranchDTO()));

        return inventoryDrug;
    }

    // Convert Drug to DrugDTO
    public static InventoryDrugDTO inventoryDrugToDto(InventoryDrug inventoryDrug) {
        if (inventoryDrug == null) {
            return null;
        }

        InventoryDrugDTO dto = new InventoryDrugDTO();

        dto.setId(inventoryDrug.getId());
        dto.setDrugDTO(drugToDto(inventoryDrug.getDrug()));
        dto.setPrice(inventoryDrug.getPrice());
        dto.setStock(inventoryDrug.getStock());
        dto.setBranchDTO(branchToDto(inventoryDrug.getBranch()));

        return dto;
    }

//    // Convert a list of DrugDTO to a list of Drug
//    public static List<InventoryDrug> dtoListToDrugList(List<InventoryDrugDTO> dtoList) {
//        return dtoList.stream().map(InventoryDrugMapper::dtoToDrug).collect(Collectors.toList());
//    }
//
//    // Convert a list of Drug to a list of DrugDTO
//    public static List<InventoryDrugDTO> drugListToDtoList(List<InventoryDrug> inventoryDrugList) {
//        return inventoryDrugList.stream().map(InventoryDrugMapper::drugToDto).collect(Collectors.toList());
//    }
}
