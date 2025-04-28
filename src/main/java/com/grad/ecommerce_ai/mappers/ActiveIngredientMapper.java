//package com.grad.ecommerce_ai.mappers;
//
//import com.grad.ecommerce_ai.dto.ActiveIngredientDTO;
//import com.grad.ecommerce_ai.entity.ActiveIngredient;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class ActiveIngredientMapper {
//
//    public static ActiveIngredient dtoToActiveIngredient(ActiveIngredientDTO dto) {
//        if (dto == null) {
//            return null;
//        }
//
//        ActiveIngredient ingredient = new ActiveIngredient();
//
//        ingredient.setId(dto.getId());
//        ingredient.setActiveIngredient(dto.getActiveIngredient());
//        ingredient.setIngredientArabicName(dto.getIngredientArabicName());
//        ingredient.setDescription(dto.getDescription());
//
//        return ingredient;
//    }
//
//
//    public static ActiveIngredientDTO activeIngredientToDto(ActiveIngredient ingredient) {
//        if (ingredient == null) {
//            return null;
//        }
//
//        ActiveIngredientDTO dto = new ActiveIngredientDTO();
//
//        dto.setId(ingredient.getId());
//        dto.setActiveIngredient(ingredient.getActiveIngredient());
//        dto.setIngredientArabicName(ingredient.getIngredientArabicName());
//        dto.setDescription(ingredient.getDescription());
//
//        return dto;
//    }
//
//
//    public static List<ActiveIngredient> dtoListToActiveIngredientList(List<ActiveIngredientDTO> dtoList) {
//        return dtoList.stream().map(ActiveIngredientMapper::dtoToActiveIngredient).collect(Collectors.toList());
//    }
//
//
//    public static List<ActiveIngredientDTO> activeIngredientListToDtoList(List<ActiveIngredient> ingredientList) {
//        return ingredientList.stream().map(ActiveIngredientMapper::activeIngredientToDto).collect(Collectors.toList());
//    }
//}
