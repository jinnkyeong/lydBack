package com.loveyourdog.brokingservice.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Admin extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 엔티티 한정 identity 설정
    private Long id;
    private String sign;
    private String pwd;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    public void setRoles(List<Authority> roles) {
        this.roles = roles;
        roles.forEach(o -> o.setAdmin(this));
    }

}
