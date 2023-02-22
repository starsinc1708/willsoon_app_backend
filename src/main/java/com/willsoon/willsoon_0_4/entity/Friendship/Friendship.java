package com.willsoon.willsoon_0_4.entity.Friendship;

import com.willsoon.willsoon_0_4.entity.AppUser.AppUser;
import com.willsoon.willsoon_0_4.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "friendship")
public class Friendship extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user1_id", referencedColumnName = "id")
    private AppUser user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", referencedColumnName = "id")
    private AppUser user2;
}

