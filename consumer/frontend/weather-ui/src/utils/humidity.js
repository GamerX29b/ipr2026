export function absoluteHumidity(temperature, humidity) {
  const saturationPressure = 6.112 * Math.exp((17.62 * temperature) / (243.12 + temperature))
  const actualPressure = (humidity / 100) * saturationPressure
  return (216.7 * actualPressure) / (273.15 + temperature)
}
