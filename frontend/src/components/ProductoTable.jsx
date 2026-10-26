const formatoPrecio = new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP' })

function ProductoTable({ productos, onEditar, onEliminar }) {
  if (productos.length === 0) {
    return <p className="vacio">Todavia no hay productos registrados.</p>
  }

  return (
    <table className="tabla">
      <thead>
        <tr>
          <th>ID</th>
          <th>Nombre</th>
          <th>Precio</th>
          <th>Stock</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        {productos.map((p) => (
          <tr key={p.id}>
            <td>{p.id}</td>
            <td>{p.nombre}</td>
            <td>{formatoPrecio.format(p.precio)}</td>
            <td>{p.stock}</td>
            <td className="acciones">
              <button type="button" className="secundario" onClick={() => onEditar(p)}>
                Editar
              </button>
              <button type="button" className="peligro" onClick={() => onEliminar(p)}>
                Eliminar
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}

export default ProductoTable
