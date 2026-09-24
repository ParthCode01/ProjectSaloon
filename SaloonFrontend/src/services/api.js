import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8000"
});

export const getTenants = () => {
    return api.get("/tenants");
};

export const getTenantById = (id) => {
    return api.get(`/tenants/${id}`);
};

export const getTreatmentsByTenantId = (tenantId) => {
    return api.get(`/tenants/${tenantId}/treatments`);
};

export default api;