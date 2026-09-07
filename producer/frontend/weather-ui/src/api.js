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

export async function getHistory() {
  return parseResponse(await fetch('/api/weather/history'))
}

export async function submitWeather(payload) {
  return parseResponse(await fetch('/api/weather', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  }))
}
