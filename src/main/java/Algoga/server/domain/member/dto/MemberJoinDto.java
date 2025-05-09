package Algoga.server.domain.member.dto;

import Algoga.server.domain.member.Country;
import Algoga.server.domain.member.Disease;
import Algoga.server.domain.member.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MemberJoinDto {
    private String ID;
    private String password;
    private String name;
    private LocalDate birth;
    private String medications;

    @Enumerated(EnumType.STRING)
    private Disease disease;

    @Enumerated(EnumType.STRING)
    private Country country;

    @Enumerated(EnumType.STRING)
    private Gender gender;



}
