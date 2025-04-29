CREATE TABLE jutsus_map (
    personagem_id BIGINT,
    nome_jutsu VARCHAR(100),
    dano INTEGER NOT NULL,
    consumo_de_chakra INTEGER NOT NULL,
    PRIMARY KEY (personagem_id, nome_jutsu),
    FOREIGN KEY (personagem_id) REFERENCES personagem(id) ON DELETE CASCADE
);

ALTER TABLE personagem ADD COLUMN vida INTEGER NOT NULL DEFAULT 100;