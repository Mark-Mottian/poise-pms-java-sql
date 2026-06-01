USE PoisePMS;

INSERT INTO customers
  (first_name, surname, telephone, email, physical_address)
VALUES
  ('Tony', 'Stark', '0811111111', 'tony.stark@gmail.com', '10880 Malibu Point, Malibu'),
  ('Bruce', 'Wayne', '0822222222', 'bruce.wayne@gmail.com', '1007 Mountain Drive, Gotham');

INSERT INTO architects
  (first_name, surname, telephone, email, physical_address)
VALUES
  ('Peter', 'Parker', '0833333333', 'peter.parker@gmail.com', '20 Ingram Street, Queens'),
  ('Clark', 'Kent', '0844444444', 'clark.kent@gmail.com', '344 Clinton Street, Metropolis');

INSERT INTO contractors
  (first_name, surname, telephone, email, physical_address)
VALUES
  ('Logan', 'Howlett', '0855555555', 'logan.howlett@gmail.com', '1 Xavier Lane, Westchester'),
  ('Diana', 'Prince', '0866666666', 'diana.prince@gmail.com', '10 Themyscira Road, Gateway City');

INSERT INTO structural_engineers
  (first_name, surname, telephone, email, physical_address)
VALUES
  ('Gandalf', 'Grey', '0877777777', 'gandalf.grey@gmail.com', '1 Wizard Tower, Middle Earth'),
  ('Bilbo', 'Baggins', '0888888888', 'bilbo.baggins@gmail.com', '1 Bag End, Hobbiton');

INSERT INTO project_managers
  (first_name, surname, telephone, email, physical_address)
VALUES
  ('Frodo', 'Baggins', '0899999999', 'frodo.baggins@gmail.com', '2 Bag End, Hobbiton'),
  ('Aragorn', 'Elessar', '0800000000', 'aragorn.elessar@gmail.com', '1 White Tree Road, Gondor');

INSERT INTO projects
  (
    project_name,
    building_type,
    physical_address,
    erf_number,
    total_fee,
    amount_paid,
    deadline,
    is_finalised,
    completion_date,
    customer_id,
    architect_id,
    contractor_id,
    structural_engineer_id,
    project_manager_id
  )
VALUES
  (
    'House Stark',
    'House',
    '10880 Malibu Point, Malibu',
    'ERF1001',
    500000.00,
    250000.00,
    '2026-08-30',
    FALSE,
    NULL,
    1,
    1,
    1,
    1,
    1
  ),
  (
    'Mansion Wayne',
    'Mansion',
    '1007 Mountain Drive, Gotham',
    'ERF1002',
    950000.00,
    400000.00,
    '2026-10-15',
    FALSE,
    NULL,
    2,
    2,
    2,
    2,
    2
  );