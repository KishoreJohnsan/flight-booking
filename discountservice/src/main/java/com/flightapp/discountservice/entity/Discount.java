package com.flightapp.discountservice.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@Entity
@DynamicUpdate
public class Discount {

    @Id
    @SequenceGenerator(name="DISCOUNT_SEQ_GEN", sequenceName="DISCOUNT_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="DISCOUNT_SEQ_GEN")
    private Long discountId;
    private String discountCode;
    private String discountPercent;


}
