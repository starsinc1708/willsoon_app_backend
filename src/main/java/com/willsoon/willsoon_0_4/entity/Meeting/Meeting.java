package com.willsoon.willsoon_0_4.entity.Meeting;

import com.willsoon.willsoon_0_4.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "meeting")
public class Meeting  extends BaseEntity {
}
