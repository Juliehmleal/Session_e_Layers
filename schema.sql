CREATE DATABASE IF NOT EXISTS taskdb;
USE taskdb;

DROP TABLE IF EXISTS tarefas;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL
);

CREATE TABLE tarefas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    concluida BOOLEAN NOT NULL DEFAULT FALSE,
    usuario_id INT NOT NULL,
    CONSTRAINT fk_tarefas_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Dados de teste (senha de ambos: senha123)
INSERT INTO usuarios (id, nome, email, senha_hash) VALUES
(1, 'Usuario A', 'usuarioA@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
(2, 'Usuario B', 'usuarioB@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

INSERT INTO tarefas (titulo, concluida, usuario_id) VALUES
('Configurar ambiente Maven', TRUE, 1),
('Refatorar camada Service', FALSE, 1),
('Implementar filtro de sessao', FALSE, 1),
('Estudar arquitetura em camadas', TRUE, 2),
('Revisar script de migracao', TRUE, 2),
('Testar endpoints com curl', FALSE, 2);