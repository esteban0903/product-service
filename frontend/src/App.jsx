import { useState } from 'react'
import ProductoForm from './components/ProductoForm'
import ProductoTable from './components/ProductoTable'
import { useProductos } from './hooks/useProductos'

function App() {
  const { productos, cargando, error, guardar, eliminar } = useProductos()
  const [editando, setEditando] = useState(null)
  const [errorAccion, setErrorAccion] = useState(null)

  const handleGuardar = async (producto) => {
    await guardar(producto)
    setEditando(null)
  }

  const handleEliminar = async (producto) => {
    if (!window.confirm(`¿Eliminar "${producto.nombre}"?`)) return
    try {
      await eliminar(producto.id)
      setErrorAccion(null)
      if (editando?.id === producto.id) setEditando(null)
    } catch (e) {
      setErrorAccion(e.message)
    }
  }

  return (
    <main>
      <h1>Productos</h1>

      <ProductoForm
        key={editando?.id ?? 'nuevo'}
        producto={editando}
        onGuardar={handleGuardar}
        onCancelar={() => setEditando(null)}
      />

      <section>
        <h2>Listado</h2>
        {error && <p className="error">No se pudo cargar la lista: {error}</p>}
        {errorAccion && <p className="error">{errorAccion}</p>}
        {cargando && productos.length === 0
          ? <p>Cargando...</p>
          : <ProductoTable productos={productos} onEditar={setEditando} onEliminar={handleEliminar} />}
      </section>
    </main>
  )
}

export default App
