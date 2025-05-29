package cl.duoc.edutech.EduTech.model;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SistemaOshi {

    public static Reporte generarReporteSistema(long id) throws InterruptedException {

        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        OperatingSystem os = systemInfo.getOperatingSystem();

        // CPU
        CentralProcessor procesador = hardware.getProcessor();
        long[] prevTicks = procesador.getSystemCpuLoadTicks();
        Thread.sleep(1000);
        double cpuLoad = procesador.getSystemCpuLoadBetweenTicks(prevTicks) * 100;

        // RAM
        GlobalMemory memoria = hardware.getMemory();
        long memoriaTotal = memoria.getTotal();
        long memoriaDisponible = memoria.getAvailable();
        long memoriaUsada = memoriaTotal - memoriaDisponible;
        double porcentajeRamUtilizada = (double) memoriaUsada / memoriaTotal * 100;
        double memoriaTotalGB = bytesToGB(memoriaTotal);
        double memoriaUsadaGB = bytesToGB(memoriaUsada);

        // DISCO
        List<OSFileStore> discos = os.getFileSystem().getFileStores();
        OSFileStore discoPrincipal = discos.get(0);
        long discoTotal = discoPrincipal.getTotalSpace();
        long discoDisponible = discoPrincipal.getUsableSpace();
        long discoUsado = discoTotal - discoDisponible;
        double porcentajeDiscoUtilizado = (double) discoUsado / discoTotal * 100;
        double discoTotalGB = bytesToGB(discoTotal);
        double discoUsadoGB = bytesToGB(discoUsado);

        // Estado del sistema
        String estado;
        if (cpuLoad > 80 || porcentajeRamUtilizada > 80 || porcentajeDiscoUtilizado > 80)
            estado = "Crítico";
        else if (cpuLoad > 50 || porcentajeRamUtilizada > 60 || porcentajeDiscoUtilizado > 80)
            estado = "Alto Uso";
        else
            estado = "Operativo";

        // Mapa de detalles del reporte
        Map<String, Object> detalles = new LinkedHashMap<>();
        detalles.put("Sistema Operativo", os.getFamily());
        detalles.put("CPU Utilizada (%)", round(cpuLoad));
        detalles.put("RAM Total (GB)", round(memoriaTotalGB));
        detalles.put("RAM Utilizada (%)", round(porcentajeRamUtilizada));
        detalles.put("Disco Total (GB)", round(discoTotalGB));
        detalles.put("Disco Utilizado (%)", round(porcentajeDiscoUtilizado));

        return new Reporte(
                "Reporte de Estado del Sistema",
                "Estado del Sistema: " + estado,
                detalles
        );
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private static double bytesToGB(long bytes) {
        return bytes / (1024.0 * 1024 * 1024);
    }
}
