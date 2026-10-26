import { useState } from 'react'

const VACIO = { nombre: '', precio: '', stock: '' }

function aFormulario(producto) {
  if (!producto) return VACIO
  return {
    nombre: producto.nombre,
    precio: String(producto.precio),
    stock: String(producto.stock),
  }
}

/**
 * Sirve para crear y para editar. Si recibe un producto, arranca con sus datos.
 * El padre le cambia la `key` para reiniciarlo cuando se elige otro producto.
 */
function ProductoForm({ producto, onGuardar, onCancelar }) {
  const [form, setForm] = useState(() => aFormulario(producto))
  const [error, setError] = useState(null)
  const [enviando, setEnviando] = useState(false)

  const editando = Boolean(producto)

  const handleChange = (e) => {
    const { name, value } = e.target
    setForm((actual) => ({ ...actual, [name]: value }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setEnviando(true)
    try {
      await onGuardar({
        id: producto?.id,
        nombre: form.nombre.trim(),
        precio: Number(form.precio),
        stock: Number(form.stock),
      })
      setForm(VACIO)
      setError(null)
    } catch (err) {
      setError(err.message)
    } finally {
      setEnviando(false)
    }
  }

  return (
    <form className="form" onSubmit={handleSubmit}>
      <h2>{editando ? `Editar producto #${producto.id}` : 'Nuevo producto'}</h2>

      <label>
        Nombre
        <input name="nombre" value={form.nombre} onChange={handleChange} maxLength={100} required />
      </label>

      <label>
        Precio
        <input name="precio" type="number" min="0" step="0.01" value={form.precio} onChange={handleChange} required />
      </label>

      <label>
        Stock
        <input name="stock" type="number" min="0" step="1" value={form.stock} onChange={handleChange} required />
      </label>

      {error && <p className="error">{error}</p>}

      <div className="acciones">
        <button type="submit" disabled={enviando}>
          {enviando ? 'Guardando...' : editando ? 'Actualizar' : 'Guardar'}
        </button>
        {editando && (
          <button type="button" className="secundario" onClick={onCancelar}>
            Cancelar
          </button>
        )}
      </div>
    </form>
  )
}

export default ProductoForm
