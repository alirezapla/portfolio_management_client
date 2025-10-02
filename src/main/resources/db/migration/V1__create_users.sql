CREATE TABLE users (
       id BIGSERIAL PRIMARY KEY,
       uid UUID NOT NULL UNIQUE,
       username VARCHAR(100) NOT NULL UNIQUE,
       email VARCHAR(200) NOT NULL UNIQUE,
       password_hash VARCHAR(255) NOT NULL,
       created_at TIMESTAMP DEFAULT now(),
       updated_at TIMESTAMP DEFAULT now()
);

create table user_authority
(
       user_credential_id bigint       not null
              constraint fk_user_authority_user references users,
       user_authority     varchar(255) not null
              constraint user_authority_user_authority_check
                     check ((user_authority)::text = ANY
              ((ARRAY ['AUTHORITY_ADMIN'::character varying, 'AUTHORITY_USER'::character varying])::text[])),
    primary key (user_credential_id, user_authority)
);
