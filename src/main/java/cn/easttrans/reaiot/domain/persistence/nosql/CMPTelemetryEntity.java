package cn.easttrans.reaiot.domain.persistence.nosql;


/**
 * 水泥搅拌桩实体
 * @param ts 时间戳
 * @param paraStart 段落起点
 * @param paraEnd 段落终点
 * @param row 行
 * @param column 列
 * @param degreeX 横向倾角
 * @param degreeY 纵向倾角
 * @param depth 深度
 * @param electricExt 外钻杆电流
 * @param electricInt 内钻杆电流
 * @param flow 流量
 * @param groutingVolume 浆量
 * @param latitude 纬度
 * @param longitude 经度
 * @param pressure 压力
 * @param speed 速率
 * @param verticality 垂直度
 * @param sensorErrors 传感器报错
 * @param received 接收到的原始报文
 */
public record CMPTelemetryEntity(
        long ts,
        double paraStart,
        double paraEnd,
        int row,
        int column,
        double degreeX,
        double degreeY,
        double depth,
        double electricExt,
        double electricInt,
        double flow,
        double groutingVolume,
        double latitude,
        double longitude,
        double pressure,
        double speed,
        double verticality,
        int sensorErrors,
        String received
) {
}
