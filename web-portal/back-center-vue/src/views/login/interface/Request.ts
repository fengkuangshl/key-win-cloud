/* eslint-disable camelcase */
export interface LoginRequest {
    username: string
    password: string
    grant_type: string
    scope: string
    client_id: string
    client_secret: string
}
