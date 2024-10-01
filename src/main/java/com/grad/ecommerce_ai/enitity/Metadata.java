package com.grad.ecommerce_ai.enitity;

import lombok.Data;

@Data
public class Metadata {
    private String db_published_date;
    private int elements_per_page;
    private int total_elements;
    private int total_pages;
    private int current_page;
    private String next_page_url;

    // getters and setters
}