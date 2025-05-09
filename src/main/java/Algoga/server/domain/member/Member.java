package Algoga.server.domain.member;

import Algoga.server.domain.member.dto.MemberJoinDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false, nullable = false)
    private Long memberId;
    @Column(name = "id", nullable = false)
    private String ID;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "birth")
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    @Column(name = "disease")
    private Disease disease;

    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    private Country country;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    private String medications;

    public Member(MemberJoinDto memberJoinDto) {
        this.ID = memberJoinDto.getID();
        this.password = memberJoinDto.getPassword();
        this.name = memberJoinDto.getName();
        this.birth = memberJoinDto.getBirth();
        this.disease = memberJoinDto.getDisease();
        this.country = memberJoinDto.getCountry();
        this.gender = memberJoinDto.getGender();
        this.medications = memberJoinDto.getMedications();
    }
}

