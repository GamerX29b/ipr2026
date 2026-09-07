async function parseResponse(response) {
  if (!response.ok) {
    let message = `Ошибка сервера (${response.status})`
    try {
      const body = await response.json()
      if (body && body.message) {
        message = body.message
      }
    } catch {
    }
    throw new Error(message)
  }
  if (response.status === 204) {
    return null
  }
  return response.json()
}

export async function fetchLatest() {
  return parseResponse(await fetch('/api/weather/latest'))
}
