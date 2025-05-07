INSERT INTO categories (name, description)
VALUES
    ('Smartphones',            'Dispositivos móviles inteligentes con pantallas táctiles, conectividad 4G/5G y cámaras avanzadas'),
    ('Laptops & Computers',    'Ordenadores portátiles y de sobremesa para uso profesional, gaming y multimedia'),
    ('Wearables',              'Tecnología vestible como smartwatches, pulseras de actividad y auriculares inteligentes'),
    ('Networking & Storage',   'Equipos de red y soluciones de almacenamiento, incluyendo routers, switches y discos SSD/HDD'),
    ('Accessories & Peripherals','Accesorios y periféricos: cargadores, teclados, ratones, fundas y adaptadores');

INSERT INTO products (price, stock, category_id, description, image_url, name,active)
VALUES
    -- Smartphone gama alta
    (799.99, 25, 1,
     'Smartphone flagship con pantalla OLED de 6.7”, procesador octa‑core y triple cámara de 108 MP',
     'https://picsum.photos/seed/smartphone/600/400.jpg',
     'UltraPhone X Pro',true),

    -- Portátil ultraligero
    (1299.00, 15, 2,
     'Ultrabook de 14” con chasis de aluminio, 16 GB RAM, SSD NVMe de 1 TB y batería de larga duración',
     'https://picsum.photos/seed/laptop/600/400.jpg',
     'SlimBook 14 Aero',true),

    -- Smartwatch avanzado
    (249.50, 40, 3,
     'Reloj inteligente con monitor de ritmo cardíaco, GPS integrado, notificaciones y resistencia al agua 5 ATM',
     'https://picsum.photos/seed/wearable/600/400.jpg',
     'TimeFit Pro Watch',true),

    -- Router Wi‑Fi 6 Mesh
    (179.99, 30, 4,
     'Sistema de red mesh Wi‑Fi 6 con cobertura de hasta 500 m², cuatro antenas y velocidades de 3 Gbps',
     'https://picsum.photos/seed/networking/600/400.jpg',
     'MeshNet X3 Router',true),

    -- Auriculares inalámbricos
    (129.00, 50, 5,
     'Auriculares true wireless con cancelación activa de ruido, autonomía de 30 h y estuche de carga rápida',
     'https://picsum.photos/seed/accessory/600/400.jpg',
     'SoundBuds ANC',true);
