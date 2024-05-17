package cz.cvut.fit.sabirdan.wework.http.response;

import cz.cvut.fit.sabirdan.wework.enumeration.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMeRespond {
    private String username;
    private String firstName;
    private String lastName;
    private Sex sex;
}
