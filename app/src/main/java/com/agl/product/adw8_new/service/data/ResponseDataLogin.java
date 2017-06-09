package com.agl.product.adw8_new.service.data;

import com.agl.product.adw8_new.model.PermissionData;
import com.agl.product.adw8_new.model.Usuario;
import com.google.gson.annotations.SerializedName;

public class ResponseDataLogin {

    @SerializedName("permission_array")
    private PermissionData permission_array;

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Usuario usuario;

    public PermissionData getPermission_array() {
        return permission_array;
    }

    public void setPermission_array(PermissionData permission_array) {
        this.permission_array = permission_array;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
