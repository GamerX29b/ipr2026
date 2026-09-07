import { describe, expect, it } from 'vitest'
import { absoluteHumidity } from './humidity.js'

describe('absoluteHumidity (формула Магнуса)', () => {
  it('T=20 °C, RH=50% даёт около 8.6 г/м³', () => {
    expect(absoluteHumidity(20, 50)).toBeCloseTo(8.62, 1)
  })

  it('T=30 °C, RH=100% даёт около 30.3 г/м³', () => {
    expect(absoluteHumidity(30, 100)).toBeCloseTo(30.25, 1)
  })

  it('T=0 °C, RH=100% даёт около 4.8 г/м³', () => {
    expect(absoluteHumidity(0, 100)).toBeCloseTo(4.85, 1)
  })
})
