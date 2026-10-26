import { useCallback, useEffect, useState } from 'react'
import { eliminarProducto, guardarProducto, listarProductos } from '../api/productosApi'

export function useProductos() {
  const [productos, setProductos] = useState([])
  const [cargando, setCargando] = useState(true)
  const [error, setError] = useState(null)

  const cargar = useCallback(async () => {
    setCargando(true)
    try {
      setProductos(await listarProductos())
      setError(null)
    } catch (e) {
      setError(e.message)
    } finally {
      setCargando(false)
    }
  }, [])

  useEffect(() => {
    cargar()
  }, [cargar])

  const guardar = async (producto) => {
    await guardarProducto(producto)
    await cargar()
  }

  const eliminar = async (id) => {
    await eliminarProducto(id)
    await cargar()
  }

  return { productos, cargando, error, guardar, eliminar }
}
