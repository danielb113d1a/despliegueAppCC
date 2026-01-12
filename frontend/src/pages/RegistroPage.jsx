import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Login.css'; 

const RegistroPage = () => {
    const navigate = useNavigate();
    
    const [formData, setFormData] = useState({
        nombreCompleto: '', 
        username: '',       
        email: '',
        password: '',
        confirmarPassword: ''
    });

    const [mensaje, setMensaje] = useState({ texto: '', tipo: '' });
    const [isLoading, setIsLoading] = useState(false);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsLoading(true);
        setMensaje({ texto: '', tipo: '' });

        if (formData.password !== formData.confirmarPassword) {
            setMensaje({ texto: 'Las contraseñas no coinciden', tipo: 'error' });
            setIsLoading(false);
            return;
        }

        try {
            const response = await axios.post('http://localhost:8080/api/users/register', {
                username: formData.username,
                email: formData.email,
                password: formData.password,
            });

            if (response.status === 201 || response.status === 200) {
                setMensaje({ texto: '¡Cuenta creada con éxito! Redirigiendo...', tipo: 'success' });
                
                setTimeout(() => {
                    navigate('/login');
                }, 2000);
            }
        } catch (error) {
            if (error.response?.status === 409) {
                setMensaje({ texto: 'Este correo o usuario ya está registrado.', tipo: 'error' });
            } else {
                setMensaje({ texto: 'Error al conectar con el servidor.', tipo: 'error' });
            }
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="login-wrapper">
            <div className="login-card">
                <div className="login-header">
                    <h1>Crear Cuenta</h1>
                    <p>Únete a nuestra comunidad de lectores</p>
                </div>

                {mensaje.texto && (
                    <div className={mensaje.tipo === 'error' ? 'error-message' : 'success-message'}>
                        {mensaje.texto}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="login-form">
                    
                    <div className="input-group">
                        <label>Nombre Completo</label>
                        <input 
                            name="nombreCompleto" 
                            type="text" 
                            placeholder="Juan Pérez" 
                            value={formData.nombreCompleto} 
                            onChange={handleChange} 
                        />
                    </div>

                    <div className="input-group">
                        <label>Usuario</label>
                        <input 
                            name="username" 
                            type="text" 
                            placeholder="juanperez99" 
                            value={formData.username} 
                            onChange={handleChange} 
                            required 
                        />
                    </div>

                    <div className="input-group">
                        <label>Correo Electrónico</label>
                        <input 
                            name="email" 
                            type="email" 
                            placeholder="juan@ejemplo.com" 
                            value={formData.email} 
                            onChange={handleChange} 
                            required 
                        />
                    </div>

                    <div className="input-group">
                        <label>Contraseña</label>
                        <input 
                            name="password" 
                            type="password" 
                            placeholder="••••••••" 
                            value={formData.password} 
                            onChange={handleChange} 
                            required 
                        />
                    </div>

                    <div className="input-group">
                        <label>Confirmar Contraseña</label>
                        <input 
                            name="confirmarPassword" 
                            type="password" 
                            placeholder="••••••••" 
                            value={formData.confirmarPassword} 
                            onChange={handleChange} 
                            required 
                        />
                    </div>

                    <button type="submit" className="login-btn" disabled={isLoading}>
                        {isLoading ? 'Registrando...' : 'Registrarse'}
                    </button>
                </form>

                <div className="login-footer">
                    <p>¿Ya tienes cuenta? <span onClick={() => navigate('/login')} className="link">Inicia sesión aquí</span></p>
                </div>
            </div>
        </div>
    );
};

export default RegistroPage;