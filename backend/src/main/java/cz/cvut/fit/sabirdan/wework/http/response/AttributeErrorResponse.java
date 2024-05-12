package cz.cvut.fit.sabirdan.wework.http.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
public class AttributeErrorResponse {
    private ArrayList<AttributeError> errors;

    public AttributeErrorResponse() {
        errors = new ArrayList<>();
    }

    public AttributeErrorResponse(String attribute, String message) {
        errors = new ArrayList<>();
        setAttributeError(attribute, message);
    }

    public AttributeErrorResponse(AttributeError attributeError) {
        errors = new ArrayList<>();
        setAttributeError(attributeError);
    }

    public void setAttributeError(String attribute, String message) {
        errors.add(new AttributeError(attribute, message));
    }

    public void setAttributeError(AttributeError attributeError) {
        errors.add(attributeError);
    }
}
