const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'
const BASE = `${API_URL}/productos`

async function request(url, options = {}) {
  const response = await fetch(url, options)

  if (!response.ok) {
    const error = await response.json().catch(() => null)
    const detalle = error?.detalles?.length ? `: ${error.detalles.join(', ')}` : ''
    throw new Error((error?.mensaje ?? `Error ${response.status}`) + detalle)
  }

  // 204 No Content no trae cuerpo
  return response.status === 204 ? null : response.json()
}

export function listarProductos() {
  return request(BASE)
}

/** Si el producto trae id el backend lo actualiza, si no lo crea. */
export function guardarProducto(producto) {
  return request(BASE, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(producto),
  })
}

export function eliminarProducto(id) {
  return request(`${BASE}/${id}`, { method: 'POST' })
}
