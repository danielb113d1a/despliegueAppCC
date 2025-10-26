// frontend/src/App.test.jsx

import React from 'react';
// Importa 'waitFor' para el test asíncrono
import { render, screen, waitFor } from '@testing-library/react';
import App from './App';

// Estilo BDD: "describe" el componente
describe('App Component', () => {

  // --- 1. TU TEST ACTUAL (Este pasará) ---
  it('should render the main headline', () => {
    // Arrange
    render(<App />);

    // Assert
    const headline = screen.getByText(/Bienvenido a CloudLibrary/i);
    expect(headline).toBeInTheDocument();
  });

  /*
  // --- 2. NUEVO TEST (Fallará) ---
  // Requisito: "Los usuarios deben poder autenticarse"
  it('should render a "Login" button', () => {
    // Arrange
    render(<App />);

    // Act
    // (Buscamos un botón con el texto "Login" o "Iniciar Sesión")
    const loginButton = screen.getByRole('button', { name: /login|iniciar sesión/i });

    // Assert
    // Esto fallará porque el botón no existe
    expect(loginButton).toBeInTheDocument();
  });


  // --- 3. NUEVO TEST (Fallará) ---
  // Requisito: "Los usuarios deben poder buscar libros"
  it('should render a search bar', () => {
    // Arrange
    render(<App />);

    // Act
    // (Buscamos un input de tipo "textbox" con una etiqueta de "Buscar")
    const searchInput = screen.getByRole('textbox', { name: /buscar libro/i });

    // Assert
    // Esto fallará porque el input no existe
    expect(searchInput).toBeInTheDocument();
  });


  // --- 4. NUEVO TEST ASÍNCRONO (Fallará) ---
  // Requisito: "La app debe mostrar una lista de libros"
  // (Usamos async/await y 'findBy' para tests asíncronos)
  it('should render a list of books fetched from an API', async () => {
    // Arrange
    render(<App />);

    // Act
    // (Simulamos que la app hace un fetch)
    // 'findBy...' espera hasta 1 segundo a que aparezca el elemento
    // Buscamos un elemento de lista (li) que contenga "Clean Code"
    const bookTitle = await screen.findByText(/Clean Code/i);

    // Assert
    // Esto fallará porque no hay ningún fetch y "Clean Code" nunca aparecerá
    expect(bookTitle).toBeInTheDocument();
  });
  */

});