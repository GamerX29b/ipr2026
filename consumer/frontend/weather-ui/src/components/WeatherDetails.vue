<script setup>
import { formatDateTime } from '../format.js'

defineProps({
  record: { type: Object, required: true },
  absoluteHumidity: { type: String, required: true }
})
</script>

<template>
  <section class="block">
    <h2 class="block-title">Подробности</h2>
    <dl class="details-grid">
      <dt>Дата и время добавления</dt>
      <dd>{{ formatDateTime(record.createdAt) }}</dd>
      <dt>Температура</dt>
      <dd>{{ Number(record.temperature).toFixed(1) }} °C</dd>
      <dt>Влажность</dt>
      <dd>{{ record.humidity }} %</dd>
      <dt>Направление ветра</dt>
      <dd>{{ record.windDirectionLabel }}</dd>
      <dt>Абсолютная влажность</dt>
      <dd>{{ absoluteHumidity }} г/м³</dd>
    </dl>
  </section>
</template>
