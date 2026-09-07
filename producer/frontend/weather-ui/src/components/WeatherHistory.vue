<script setup>
import { formatDateTime } from '../format.js'

defineProps({
  records: { type: Array, default: () => [] }
})
</script>

<template>
  <div v-if="records.length" class="table-wrap">
    <table>
      <thead>
        <tr>
          <th>№</th>
          <th>Температура, °C</th>
          <th>Влажность, %</th>
          <th>Скорость ветра, м/с</th>
          <th>Направление ветра</th>
          <th>Дата и время добавления</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="record in records" :key="record.id">
          <td>{{ record.id }}</td>
          <td>{{ record.temperature }}</td>
          <td>{{ record.humidity }}</td>
          <td>{{ record.windSpeed }}</td>
          <td>{{ record.windDirectionLabel }}</td>
          <td>{{ formatDateTime(record.createdAt) }}</td>
        </tr>
      </tbody>
    </table>
  </div>
  <p v-else class="hint">Пока нет переданных сведений</p>
</template>
