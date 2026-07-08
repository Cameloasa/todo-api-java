package dev.cameloasa.todoapi.unit.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.cameloasa.todoapi.converter.RoleConverter;
import dev.cameloasa.todoapi.converter.UserConverter;
import dev.cameloasa.todoapi.converter.UserConverterImpl;
import dev.cameloasa.todoapi.domanin.dto.UserDTOForm;
import dev.cameloasa.todoapi.unit.fixtures.RoleFixture;
import dev.cameloasa.todoapi.unit.fixtures.UserFixture;

class UserConverterTest {

    @Mock
    private RoleConverter roleConverter;

    private UserConverter converter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        converter = new UserConverterImpl(roleConverter);
    }

    @Test
    void toUserDTOView_should_return_null_when_entity_is_null() {
        assertNull(converter.toUserDTOView(null));
    }

    @Test
    void toUserDTOView_should_map_basic_fields() {
        var user = UserFixture.sampleUser();
        var role = RoleFixture.sampleRole();
        var roleDTO = RoleFixture.sampleRoleDTOView();

        user.setRoles(Set.of(role));

        when(roleConverter.toRoleDTOView(role)).thenReturn(roleDTO);

        var dto = converter.toUserDTOView(user);

        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.isExpired(), dto.isExpired());
        assertEquals(1, dto.getRoles().size());
        assertEquals(roleDTO.getName(), dto.getRoles().iterator().next().getName());
    }

    @Test
    void toUserEntity_should_return_null_when_dto_is_null() {
        assertNull(converter.toUserEntity(null));
    }

    @Test
    void toUserEntity_should_map_basic_fields() {
        var dto = new UserDTOForm();
        dto.setEmail("test@example.com");
        dto.setUsername("test");
        dto.setPassword("Password123!");
        dto.setExpired(false);

        var entity = converter.toUserEntity(dto);

        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getUsername(), entity.getUsername());
        assertEquals(dto.getPassword(), entity.getPassword());
        assertEquals(dto.isExpired(), entity.isExpired());
    }
}

