// frontend/src/App.test.jsx
import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import App from './App';

describe('App Component', () => {

  it('should render the main headline', () => {
    render(<App />);
    const headline = screen.getByText(/Bienvenido a CloudLibrary/i);
    expect(headline).toBeInTheDocument();
  });

  /*it('should render the Login button', () => {
    render(<App />);
    const loginButton = screen.getByRole('button', { name: /login/i });
    expect(loginButton).toBeInTheDocument();
  });

  it('should render the Register button', () => {
    render(<App />);
    const registerButton = screen.getByRole('button', { name: /register/i });
    expect(registerButton).toBeInTheDocument();
  });

  it('should trigger an action when Login button is clicked', () => {
    render(<App />);
    const loginButton = screen.getByRole('button', { name: /login/i });

    // Mock de función o evento
    fireEvent.click(loginButton);

    // Aquí podrías comprobar un cambio de UI, un modal, un redirect, etc.
    // Ejemplo: pantalla de login aparece
    const loginForm = screen.getByTestId('login-form');
    expect(loginForm).toBeInTheDocument();
  });

  it('should trigger an action when Register button is clicked', () => {
    render(<App />);
    const registerButton = screen.getByRole('button', { name: /register/i });

    fireEvent.click(registerButton);

    // Ejemplo: pantalla de registro aparece
    const registerForm = screen.getByTestId('register-form');
    expect(registerForm).toBeInTheDocument();
  });*/
});
