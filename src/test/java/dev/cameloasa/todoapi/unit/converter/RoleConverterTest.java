package dev.cameloasa.todoapi.unit.converter;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


import org.junit.jupiter.api.Test;

import dev.cameloasa.todoapi.converter.RoleConverterImpl;
import dev.cameloasa.todoapi.unit.fixtures.RoleFixture;

class RoleConverterTest {

    private final RoleConverterImpl converter = new RoleConverterImpl();

    @Test
    void toRoleDTOView_should_return_null_when_entity_is_null() {
        assertNull(converter.toRoleDTOView(null));
    }

    @Test
    void toRoleDTOView_should_map_fields_correctly() {
        var entity = RoleFixture.sampleRole();

        var dto = converter.toRoleDTOView(entity);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    void toRoleEntity_should_return_null_when_dto_is_null() {
        assertNull(converter.toRoleEntity(null));
    }

    @Test
    void toRoleEntity_should_map_fields_correctly() {
        var dto = RoleFixture.sampleRoleForm();

        var entity = converter.toRoleEntity(dto);

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }
}

