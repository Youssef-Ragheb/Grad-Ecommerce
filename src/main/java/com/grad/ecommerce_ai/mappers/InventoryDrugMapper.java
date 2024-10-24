package com.grad.ecommerce_ai.mappers;

import com.grad.ecommerce_ai.dto.InventoryDrugDTO;
import com.grad.ecommerce_ai.enitity.InventoryDrug;


public class InventoryDrugMapper {

//
//    public static InventoryDrug dtoToDrug(InventoryDrugDTO dto) {
//        if (dto == null) {
//            return null;
//        }
//
//        InventoryDrug inventoryDrug = new InventoryDrug();
//
//        inventoryDrug.setId(dto.getId());
//        inventoryDrug.setDrugId(dto.getDrugId());
//        inventoryDrug.setCategoryId(dto.getCategoryId());
//        inventoryDrug.setActiveIngredientId(dto.getActiveIngredientId());
//        inventoryDrug.setPrice(dto.getPrice());
//        inventoryDrug.setStock(dto.getStock());
//        inventoryDrug.setBranchId(dto.getBranchId());
//
//        return inventoryDrug;
//    }

    // Convert Drug to DrugDTO
    public static InventoryDrugDTO drugToDto(InventoryDrug inventoryDrug) {
        if (inventoryDrug == null) {
            return null;
        }

        InventoryDrugDTO dto = new InventoryDrugDTO();

        dto.setId(inventoryDrug.getId());
        dto.setDrugId(inventoryDrug.getDrugId());
        dto.setCategoryId(inventoryDrug.getCategoryId());
        dto.setActiveIngredientId(inventoryDrug.getActiveIngredientId());
        dto.setPrice(inventoryDrug.getPrice());
        dto.setStock(inventoryDrug.getStock());
        dto.setBranchId(inventoryDrug.getBranchId());

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
