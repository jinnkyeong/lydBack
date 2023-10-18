package com.loveyourdog.brokingservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.loveyourdog.brokingservice.model.dto.querydsl.AuthorityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String name;

    @JoinColumn(name="dogwalker")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Dogwalker dogwalker;

    @JoinColumn(name="customer")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Customer customer;

    @JoinColumn(name="admin")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Admin admin;

    // setter
    public void setDogwalker(Dogwalker dogwalker) {
        this.dogwalker = dogwalker;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public void setAdmin(Admin admin) {
        this.admin = admin;
    }


    public AuthorityDto toAuthorityDto() throws Exception {
        AuthorityDto authorityDto = new AuthorityDto();
        authorityDto.setId(this.getId());
        authorityDto.setName(this.getName());
        if(this.getDogwalker().getId()!=null){
            authorityDto.setDogwalkerId(this.getDogwalker().getId());
        }else {
            throw new Exception("도그워커id 없음");
        }
        if(this.getCustomer().getId()!=null){
            authorityDto.setCustomerId(this.getCustomer().getId());
        } else {
            throw  new Exception("customerid 없음");
        }
        if(this.getAdmin().getId()!=null){
            authorityDto.setAdminId(this.getAdmin().getId());
        }else {
            throw new Exception("어드민id 없음");
        }
        return authorityDto;
    }
}
