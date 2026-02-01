package com.blink.backend.domain.chat.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import jakarta.annotation.PreDestroy

@Service
class MessageQueueService {
    private val logger = LoggerFactory.getLogger(MessageQueueService::class.java)
    private val queues = ConcurrentHashMap<String, ExecutorService>()

    fun submitMessage(clinicCode: String, phoneNumber: String, task: Runnable) {
        val key = "$clinicCode:$phoneNumber"
        val executor = queues.computeIfAbsent(key) {
            logger.debug("Creating new message queue for key: {}", key)
            Executors.newSingleThreadExecutor { runnable ->
                Thread(runnable, "msg-queue-$key").apply { isDaemon = true }
            }
        }
        executor.submit {
            try {
                task.run()
            } catch (e: Exception) {
                logger.error("Error processing message for key {}: {}", key, e.message, e)
            }
        }
    }

    @PreDestroy
    fun shutdown() {
        logger.info("Shutting down {} message queues", queues.size)
        queues.values.forEach { executor ->
            executor.shutdown()
        }
    }
}
