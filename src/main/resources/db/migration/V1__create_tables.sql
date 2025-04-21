CREATE TABLE personagem (
    id BIGSERIAL PRIMARY KEY,
    tipo_ninja VARCHAR(20),
    nome VARCHAR(100) NOT NULL,
    idade INTEGER NOT NULL,
    aldeia VARCHAR(100) NOT NULL,
    chakra INTEGER NOT NULL,
    CONSTRAINT tipo_ninja_check CHECK (tipo_ninja IN ('TAIJUTSU', 'NINJUTSU', 'GENJUTSU'))
);

CREATE TABLE jutsus (
    personagem_id BIGINT,
    jutsus VARCHAR(100),
    FOREIGN KEY (personagem_id) REFERENCES personagem(id) ON DELETE CASCADE
);