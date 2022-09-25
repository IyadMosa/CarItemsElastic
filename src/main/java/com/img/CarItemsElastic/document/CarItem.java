package com.img.CarItemsElastic.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import static com.img.CarItemsElastic.helper.Constants.INDEX_NAME;

@Data
@Document(indexName = INDEX_NAME)
public class CarItem {

    @Id
    @Field(type = FieldType.Keyword)
    private Long id;

    @Field(type = FieldType.Long)
    private Long price;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String maker;

    @Field(type = FieldType.Text)
    private String model;

    @Field(type = FieldType.Text)
    private String year;
}
