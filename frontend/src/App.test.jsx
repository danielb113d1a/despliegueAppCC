// frontend/src/App.test.jsx
import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

// Estilo BDD: "describe" el componente
describe('App Component', () => {

  // Estilo BDD: "it" (o "test") describe el comportamiento
  it('should render the main headline', () => {
    
    // 1. Arrange (Organizar)
    render(<App />);

    // 2. Act (Actuar)
    // (No hay acción en este test, solo renderizado)

    // 3. Assert (Afirmar) - Usando Jest y RTL
    const headline = screen.getByText(/Bienvenido a CloudLibrary/i);
    expect(headline).toBeInTheDocument();
  });
});