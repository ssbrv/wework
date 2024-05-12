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
        addAttributeError(attribute, message);
    }

    public AttributeErrorResponse(AttributeError attributeError) {
        errors = new ArrayList<>();
        addAttributeError(attributeError);
    }

    public void addAttributeError(String attribute, String message) {
        errors.add(new AttributeError(attribute, message));
    }

    public void addAttributeError(AttributeError attributeError) {
        errors.add(attributeError);
    }
}
