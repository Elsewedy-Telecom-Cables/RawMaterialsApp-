IF DB_ID('material_testing') IS NULL EXECUTE('CREATE DATABASE [material_testing];');
GO

USE [material_testing];
GO

IF SCHEMA_ID('dbo') IS NULL EXECUTE('CREATE SCHEMA [dbo];');
GO

CREATE  TABLE material_testing.dbo.countries (
                                                 country_id           int    IDENTITY  NOT NULL,
                                                 country_name         nvarchar(100)      NOT NULL,
                                                 CONSTRAINT pk_countries PRIMARY KEY CLUSTERED ( country_id  asc ) ,
                                                 CONSTRAINT unq_countries UNIQUE ( country_name  asc )
);
GO

CREATE  TABLE material_testing.dbo.material_documents (
                                                          material_doc_id      int    IDENTITY  NOT NULL,
                                                          material_doc_name    nvarchar(150)      NOT NULL,
                                                          CONSTRAINT pk_material_document PRIMARY KEY CLUSTERED ( material_doc_id  asc )
);
GO

CREATE  TABLE material_testing.dbo.materials (
                                                 material_id          int    IDENTITY  NOT NULL,
                                                 material_name        nvarchar(150)      NOT NULL,
                                                 item_code            nvarchar(150)      NULL,
                                                 CONSTRAINT pk_materials PRIMARY KEY CLUSTERED ( material_id  asc ) ,
                                                 CONSTRAINT unq_materials UNIQUE ( material_name  asc )
);
GO

CREATE  TABLE material_testing.dbo.samples (
                                               sample_id            int    IDENTITY ( 1 , 1 )  NOT NULL,
                                               sample_name          nvarchar(100)      NOT NULL,
                                               CONSTRAINT pk_Tbl PRIMARY KEY  ( sample_id ) ,
                                               CONSTRAINT unq_samples UNIQUE ( sample_name )
);
GO

CREATE  TABLE material_testing.dbo.sections (
                                                section_id           int    IDENTITY  NOT NULL,
                                                section_name         varchar(100)      NOT NULL,
                                                CONSTRAINT pk_sections PRIMARY KEY CLUSTERED ( section_id  asc ) ,
                                                CONSTRAINT unq_sections UNIQUE ( section_name  asc )
);
GO

CREATE  TABLE material_testing.dbo.suppliers (
                                                 supplier_id          int    IDENTITY  NOT NULL,
                                                 supplier_name        varchar(120)      NOT NULL,
                                                 supplier_code        varchar(100)      NULL,
                                                 CONSTRAINT pk_suppliers PRIMARY KEY CLUSTERED ( supplier_id  asc ) ,
                                                 CONSTRAINT unq_suppliers UNIQUE ( supplier_name  asc )
);
GO

CREATE  TABLE material_testing.dbo.test_names (
                                                  test_name_id         int    IDENTITY  NOT NULL,
                                                  test_name            nvarchar(150)      NOT NULL,
                                                  CONSTRAINT pk_test_names PRIMARY KEY CLUSTERED ( test_name_id  asc ) ,
                                                  CONSTRAINT unq_test_names UNIQUE ( test_name  asc )
);
GO

CREATE  TABLE material_testing.dbo.users (
                                             user_id              int    IDENTITY  NOT NULL,
                                             emp_code             int      NOT NULL,
                                             user_name            nvarchar(100)      NOT NULL,
                                             password             nvarchar(50)      NOT NULL,
                                             full_name            nvarchar(100)      NOT NULL,
                                             phone                varchar(11)      NULL,
                                             role                 int      NOT NULL,
                                             active               int      NOT NULL,
                                             creation_date        date      NULL,
                                             CONSTRAINT pk_users PRIMARY KEY CLUSTERED ( user_id  asc )
);
GO

CREATE  TABLE material_testing.dbo.supplier_country (
                                                        supplier_id          int      NOT NULL,
                                                        country_id           int      NOT NULL,
                                                        CONSTRAINT pk_supplier_country PRIMARY KEY CLUSTERED ( supplier_id  asc, country_id  asc )
);
GO

CREATE  TABLE material_testing.dbo.material_tests (
                                                      material_test_id     int    IDENTITY  NOT NULL,
                                                      section_id           int      NOT NULL,
                                                      supplier_id          int      NOT NULL,
                                                      country_id           int      NOT NULL,
                                                      material_id          int      NOT NULL,
                                                      user_id              int      NOT NULL,
                                                      po_no                nvarchar(150)      NULL,
                                                      receipt              nvarchar(150)      NULL,
                                                      total_quantity       nvarchar(100)      NULL,
                                                      quantity_accepted    nvarchar(100)      NULL,
                                                      quantity_rejected    nvarchar(150)      NULL,
                                                      creation_date        datetime2      NOT NULL,
                                                      oracle_sample        nvarchar(150)      NULL,
                                                      spqr                 nvarchar(150)      NULL,
                                                      notes                nvarchar(350)      NULL,
                                                      comment              nvarchar(300)      NULL,
                                                      CONSTRAINT pk_material_test PRIMARY KEY CLUSTERED ( material_test_id  asc )
);
GO

CREATE  TABLE material_testing.dbo.test_results (
                                                    test_result_id       int    IDENTITY  NOT NULL,
                                                    material_test_id     int      NOT NULL,
                                                    sample_id            int      NULL,
                                                    test_name_id         int      NOT NULL,
                                                    user_id              int      NOT NULL,
                                                    requirement          nvarchar(150)      NULL,
                                                    actual               nvarchar(150)      NULL,
                                                    creation_date        datetime2      NOT NULL,
                                                    test_situation       int      NULL,
                                                    CONSTRAINT pk_material_test_report_results PRIMARY KEY CLUSTERED ( test_result_id  asc )
);
GO

CREATE  TABLE material_testing.dbo.files (
                                             file_id              int    IDENTITY  NOT NULL,
                                             material_test_id     int      NOT NULL,
                                             material_doc_id      int      NOT NULL,
                                             user_id              int      NOT NULL,
                                             creation_date        datetime2      NOT NULL,
                                             file_path            nvarchar(500)      NULL,
                                             comment              nvarchar(200)      NULL,
                                             CONSTRAINT pk_files PRIMARY KEY CLUSTERED ( file_id  asc )
);
GO

ALTER TABLE material_testing.dbo.files ADD CONSTRAINT fk_files_material_document FOREIGN KEY ( material_doc_id ) REFERENCES material_testing.dbo.material_documents( material_doc_id );
GO

ALTER TABLE material_testing.dbo.files ADD CONSTRAINT fk_files_material_test FOREIGN KEY ( material_test_id ) REFERENCES material_testing.dbo.material_tests( material_test_id );
GO

ALTER TABLE material_testing.dbo.files ADD CONSTRAINT fk_files_users FOREIGN KEY ( user_id ) REFERENCES material_testing.dbo.users( user_id );
GO

ALTER TABLE material_testing.dbo.material_tests ADD CONSTRAINT fk_material_test_materials FOREIGN KEY ( material_id ) REFERENCES material_testing.dbo.materials( material_id );
GO

ALTER TABLE material_testing.dbo.material_tests ADD CONSTRAINT fk_material_test_sections FOREIGN KEY ( section_id ) REFERENCES material_testing.dbo.sections( section_id );
GO

ALTER TABLE material_testing.dbo.material_tests ADD CONSTRAINT fk_material_test_supplier_country FOREIGN KEY ( supplier_id, country_id ) REFERENCES material_testing.dbo.supplier_country( supplier_id, country_id );
GO

ALTER TABLE material_testing.dbo.material_tests ADD CONSTRAINT fk_material_test_users FOREIGN KEY ( user_id ) REFERENCES material_testing.dbo.users( user_id );
GO

ALTER TABLE material_testing.dbo.supplier_country ADD CONSTRAINT fk_supplier_country_countries FOREIGN KEY ( country_id ) REFERENCES material_testing.dbo.countries( country_id );
GO

ALTER TABLE material_testing.dbo.supplier_country ADD CONSTRAINT fk_supplier_country_suppliers FOREIGN KEY ( supplier_id ) REFERENCES material_testing.dbo.suppliers( supplier_id );
GO

ALTER TABLE material_testing.dbo.test_results ADD CONSTRAINT fk_material_test_report_results_material_test FOREIGN KEY ( material_test_id ) REFERENCES material_testing.dbo.material_tests( material_test_id );
GO

ALTER TABLE material_testing.dbo.test_results ADD CONSTRAINT fk_material_test_report_results_test_names FOREIGN KEY ( test_name_id ) REFERENCES material_testing.dbo.test_names( test_name_id );
GO

ALTER TABLE material_testing.dbo.test_results ADD CONSTRAINT fk_material_test_report_results_users FOREIGN KEY ( user_id ) REFERENCES material_testing.dbo.users( user_id );
GO

ALTER TABLE material_testing.dbo.test_results ADD CONSTRAINT fk_test_results_samples FOREIGN KEY ( sample_id ) REFERENCES material_testing.dbo.samples( sample_id );
GO
