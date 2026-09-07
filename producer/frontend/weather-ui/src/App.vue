<script setup>
import { onMounted, ref } from 'vue'
import WeatherForm from './components/WeatherForm.vue'
import WeatherHistory from './components/WeatherHistory.vue'
import { getHistory } from './api.js'

const records = ref([])
const loadError = ref('')

async function loadHistory() {
  try {
    records.value = await getHistory()
    loadError.value = ''
  } catch (error) {
    loadError.value = error.message
  }
}

onMounted(loadHistory)
</script>

<template>
  <main class="page">
    <section class="block">
      <h1 class="block-title">Погодные сведения</h1>
      <WeatherForm @saved="loadHistory" />
    </section>

    <section class="block">
      <h2 class="block-title">История</h2>
      <p v-if="loadError" class="error-text">{{ loadError }}</p>
      <WeatherHistory :records="records" />
    </section>
  </main>
</template>
