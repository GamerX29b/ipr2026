<script setup>
import { computed, onMounted, onUnmounted, ref } from 'vue'
import WeatherDetails from './components/WeatherDetails.vue'
import { fetchLatest } from './api.js'
import { absoluteHumidity } from './utils/humidity.js'

const POLL_INTERVAL_MS = 3000

const record = ref(null)
const detailsOpen = ref(false)
const error = ref('')
let timer = null

const temperatureText = computed(() => {
  if (!record.value) {
    return ''
  }
  return Number(record.value.temperature).toFixed(1)
})

const absoluteHumidityText = computed(() => {
  if (!record.value) {
    return ''
  }
  const value = absoluteHumidity(Number(record.value.temperature), Number(record.value.humidity))
  return value.toFixed(1)
})

async function load() {
  try {
    record.value = await fetchLatest()
    error.value = ''
  } catch {
    error.value = 'Не удалось получить сведения о погоде'
  }
}

function toggleDetails() {
  if (record.value) {
    detailsOpen.value = !detailsOpen.value
  }
}

onMounted(() => {
  load()
  timer = setInterval(load, POLL_INTERVAL_MS)
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<template>
  <main class="page">
    <section class="block summary-block" :class="{ clickable: record }" @click="toggleDetails">
      <template v-if="record">
        <div class="big-value">{{ temperatureText }} °C</div>
        <div class="small-value">Влажность: {{ record.humidity }} %</div>
        <div class="small-value">Абсолютная влажность: {{ absoluteHumidityText }} г/м³</div>
        <p class="hint">
          {{ detailsOpen ? 'Нажмите, чтобы скрыть подробности' : 'Нажмите, чтобы открыть подробности' }}
        </p>
      </template>
      <template v-else>
        <p class="hint">{{ error || 'Сведений о погоде пока нет' }}</p>
      </template>
    </section>

    <WeatherDetails v-if="record && detailsOpen" :record="record" :absolute-humidity="absoluteHumidityText" />
  </main>
</template>
