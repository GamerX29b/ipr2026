<script setup>
import { reactive, ref } from 'vue'
import { submitWeather, getRealWeather } from '../api.js'

const emit = defineEmits(['saved'])

const DIRECTIONS = [
  { value: 'NORTH', label: 'Север' },
  { value: 'NORTH_EAST', label: 'Северо-восток' },
  { value: 'EAST', label: 'Восток' },
  { value: 'SOUTH_EAST', label: 'Юго-восток' },
  { value: 'SOUTH', label: 'Юг' },
  { value: 'SOUTH_WEST', label: 'Юго-запад' },
  { value: 'WEST', label: 'Запад' },
  { value: 'NORTH_WEST', label: 'Северо-запад' }
]

const form = reactive({ temperature: '', humidity: '', windSpeed: '', windDirection: '' })
const invalid = reactive({ temperature: false, humidity: false, windSpeed: false, windDirection: false })
const submitting = ref(false)
const serverError = ref('')
const fetchingReal = ref(false)
const realWeatherInfo = ref('')

function isEmpty(value) {
  return value === '' || value === null || value === undefined
}

async function fillFromReal() {
  serverError.value = ''
  realWeatherInfo.value = ''
  fetchingReal.value = true
  try {
    const real = await getRealWeather()
    form.temperature = String(real.temperature)
    form.humidity = String(real.humidity)
    form.windSpeed = String(real.windSpeed)
    form.windDirection = real.windDirection
    invalid.temperature = false
    invalid.humidity = false
    invalid.windSpeed = false
    invalid.windDirection = false
    realWeatherInfo.value = `Загружены реальные данные: ${real.location}, измерено ${real.measuredAt}`
  } catch (error) {
    serverError.value = error.message
  } finally {
    fetchingReal.value = false
  }
}

async function onSubmit() {
  serverError.value = ''
  let filled = true
  for (const field of Object.keys(form)) {
    invalid[field] = isEmpty(form[field])
    if (invalid[field]) {
      filled = false
    }
  }
  if (!filled) {
    return
  }

  submitting.value = true
  try {
    await submitWeather({
      temperature: Number(form.temperature),
      humidity: Number(form.humidity),
      windSpeed: Number(form.windSpeed),
      windDirection: form.windDirection
    })
    form.temperature = ''
    form.humidity = ''
    form.windSpeed = ''
    form.windDirection = ''
    emit('saved')
  } catch (error) {
    serverError.value = error.message
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <form class="weather-form" novalidate @submit.prevent="onSubmit">
    <div class="fields">
      <label class="field">
        <span>Температура, °C</span>
        <input
          v-model="form.temperature"
          :class="{ invalid: invalid.temperature }"
          type="number"
          step="any"
          placeholder="Например, 21.5"
        />
      </label>
      <label class="field">
        <span>Влажность, %</span>
        <input
          v-model="form.humidity"
          :class="{ invalid: invalid.humidity }"
          type="number"
          min="0"
          max="100"
          step="1"
          placeholder="Например, 65"
        />
      </label>
      <label class="field">
        <span>Скорость ветра, м/с</span>
        <input
          v-model="form.windSpeed"
          :class="{ invalid: invalid.windSpeed }"
          type="number"
          step="any"
          placeholder="Например, 3.5"
        />
      </label>
      <label class="field">
        <span>Направление ветра</span>
        <select
          v-model="form.windDirection"
          :class="{ invalid: invalid.windDirection }"
        >
          <option value="" disabled>— выберите —</option>
          <option v-for="direction in DIRECTIONS" :key="direction.value" :value="direction.value">
            {{ direction.label }}
          </option>
        </select>
      </label>
    </div>
    <p v-if="serverError" class="error-text">{{ serverError }}</p>
    <p v-else-if="realWeatherInfo" class="hint">{{ realWeatherInfo }}</p>
    <div class="form-actions">
      <button type="submit" :disabled="submitting">Передать</button>
      <button type="button" :disabled="fetchingReal" @click="fillFromReal">
        {{ fetchingReal ? 'Запрашиваем погоду…' : 'Запросить реальную погоду' }}
      </button>
    </div>
  </form>
</template>
