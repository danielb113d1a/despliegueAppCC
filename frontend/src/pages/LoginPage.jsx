import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { loginUser } from '../services/api';
import './Login.css'; 

const Login = ({ setIsLoggedIn }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false); 
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        try {
            const data = await loginUser({ email, password });
            
            localStorage.setItem('token', data.token);
            if (setIsLoggedIn) setIsLoggedIn(true);

			localStorage.setItem('email', email);
            
            setTimeout(() => {
                navigate('/'); 
            }, 500);
            
        } catch (err) {
            console.error(err);
            setError('Credenciales incorrectas. Inténtalo de nuevo.');
            setIsLoading(false);
        }
    };

    return (
        <div className="login-wrapper">
            <div className="login-card">
                <div className="login-header">
                    <h1>Bienvenido</h1>
                    <p>Accede a tu biblioteca en la nube</p>
                </div>

                <form onSubmit={handleSubmit} className="login-form">
                    <div className="input-group">
                        <label htmlFor="email">Correo Electrónico</label>
                        <input 
                            id="email"
                            type="email" 
                            placeholder="ejemplo@correo.com"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)} 
                            required 
                        />
                    </div>

                    <div className="input-group">
                        <label htmlFor="password">Contraseña</label>
                        <input 
                            id="password"
                            type="password" 
                            placeholder="••••••••"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)} 
                            required 
                        />
                    </div>

                    {error && <div className="error-message">{error}</div>}

                    <button type="submit" className="login-btn" disabled={isLoading}>
                        {isLoading ? 'Entrando...' : 'Iniciar Sesión'}
                    </button>
                </form>

                <div className="login-footer">
                    <p>¿No tienes cuenta? <span onClick={() => navigate('/register')} className="link">Regístrate aquí</span></p>
                </div>
            </div>
        </div>
    );
};

export default Login;