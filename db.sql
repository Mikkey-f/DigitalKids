CREATE DATABASE digitalKids;
CREATE TABLE role (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL
);

CREATE TABLE Permission (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL
);

CREATE TABLE role_permission (
                                 role_id BIGINT NOT NULL ,
                                 permission_id BIGINT
);

CREATE TABLE user_role (
                           user_id BIGINT NOT NULL ,
                           role_id VARCHAR(255) NOT NULL
);

create table user
(
    id          bigint auto_increment
        primary key,
    name        varchar(32)                         not null,
    password    varchar(64)                         not null,
    avatar      varchar(500)                        not null,
    gender      enum ('男', '女', '其他')           null,
    phone       varchar(11)                         not null,
    location    varchar(200) charset utf8mb4        null,
    create_time timestamp default CURRENT_TIMESTAMP null,
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
)
    collate = utf8mb3_bin;

INSERT INTO role (id, name) VALUES
                                (1, 'admin'),
                                (2, 'user'),
                                (3, 'guest');

INSERT INTO permission (id, name) VALUES
    (1, 1);

INSERT INTO role_permission (role_id, permission_id) VALUES
                                                         (1, 1),
                                                         (2, 1);

INSERT INTO user_role (user_id, role_id) VALUES
    (1, 2);

CREATE TABLE favorite (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          entity_type INT NOT NULL,
                          entity_id INT NOT NULL,
                          user_id BIGINT NOT NULL,
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);




DROP TABLE IF EXISTS `Kid`;
CREATE TABLE `Kid` (
                       `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '孩子唯一标识',
                       `user_id` BIGINT NOT NULL COMMENT '关联的用户ID',
                       `avatar` VARCHAR(500) COLLATE utf8mb4_bin DEFAULT '/default_avatar.png' COMMENT '头像URL',
                       `nickname` VARCHAR(50) COLLATE utf8mb4_bin NOT NULL COMMENT '昵称',
                       `old` bigint COMMENT '年龄',
                       `gender` int COMMENT '性别',
                       `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                       `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='孩子信息表';

DROP TABLE IF EXISTS `parenting_encyclopedia`;
CREATE TABLE parenting_encyclopedia (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        stage INT NOT NULL COMMENT '0:备孕期, 1:孕产期管理, 2:产褥期, 3:产后恢复, 4:0-1岁宝宝, 5:1-2岁宝宝, 6:2-3岁宝宝, 7:3-5岁宝宝, 8:5-10岁宝宝, 9:10-15岁宝宝',
                                        user_id BIGINT NOT NULL,
                                        title VARCHAR(255) NOT NULL,
                                        content TEXT NOT NULL,
                                        `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
);

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (0, 1, '200字简明版：备孕基础指南', '备孕是优生优育的关键阶段。夫妻双方需提前3-6个月准备：女性每日补充400μg叶酸预防胎儿神经管畸形，男性同步补充锌元素提升精子质量。建议戒除烟酒、咖啡每日不超过200ml。保持BMI指数18.5-24.9，超重者每周减重0.5kg为宜。基础体温监测排卵周期，月经后第10天开始每日晨起测量。同房频率保持每周2-3次，排卵期隔日一次。接触化学制剂、辐射的工作者需提前3个月调整岗位。建议孕前检查涵盖TORCH五项、甲状腺功能及遗传病筛查。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (0, 1, '500字进阶版：科学备孕全解析', '营养准备
备孕夫妻需建立「营养银行」：女性重点补充叶酸（绿叶菜+补剂）、铁（红肉、动物血）、维生素D（日晒+深海鱼），男性增加精氨酸（海参、鳝鱼）和维生素E（坚果）。建议每日摄入12种以上食物，控制汞含量高的鱼类（金枪鱼/旗鱼每周≤100g）。

周期管理
基础体温法+排卵试纸联合监测：月经干净后第5天开始检测LH激素，体温升高0.3-0.5℃预示排卵。超声监测可精准定位卵泡发育，18-25mm为成熟标准。子宫内膜厚度需达8-12mm才具备最佳着床条件。

环境优化
新装修房屋至少通风6个月，甲醛浓度需≤0.08mg/m³。避免染发烫发，化妆品选择无邻苯二甲酸酯产品。电子设备使用时保持50cm距离，微波炉运行时离开2米以上。建议进行家庭水质检测，铅含量需<15μg/L。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (0, 1, '800字实操版：备孕全流程管理', '医疗准备阶段（孕前3月）
完成必检项目：女性性激素六项（月经第2-5天）、AMH卵巢储备检测、HPV+TCT筛查。男性精液分析注意禁欲3-5天后检测，正常标准：浓度≥15M/ml，活力PR≥32%。特殊人群需加查：地中海贫血基因（两广地区）、囊性纤维化（欧裔）。

饮食调整方案
制定「彩虹食谱」：每日红色食物（番茄、红椒）含番茄红素保护生殖细胞；紫色食物（蓝莓、紫甘蓝）提供花青素抗氧化。警惕隐形风险：腊肉亚硝酸盐≤30mg/kg，咖啡因总量每日<200mg（含巧克力、可乐）。

运动处方
推荐每周150分钟中等强度运动：备孕瑜伽改善盆腔血流，凯格尔运动增强盆底肌力。BMI超标者选择游泳（每周3次，每次40分钟）减轻关节压力。避免高温瑜伽、马拉松等极端运动。

心理调适技巧
正念冥想每日15分钟降低皮质醇水平，音乐疗法选择α波频率（8-14Hz）曲目。夫妻共同参加备孕课堂，建立「生育日记」记录情绪变化。遇到连续6个月未孕应及时寻求生殖科帮助。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (0, 1, '1100字专家版：精准备孕策略', '营养基因组学干预
MTHFR基因检测指导叶酸补充：CC型每日400μg，CT型600μg，TT型需活性叶酸（5-MTHF）800μg。VDR基因多态性影响维生素D吸收，FokI突变型需每日补充2000IU。建议进行营养代谢检测（如有机酸检测）排查隐性缺乏。

内分泌时钟校准
月经周期调理：黄体期不足者（高温期<11天）补充维生素B6 100mg/d。多囊卵巢综合征患者采用肌醇（2000mg/d）+NAC（600mg/d）联合方案。甲状腺TSH控制目标：备孕期≤2.5mIU/L，TPOAb阳性者需≤1.5mIU/L。

环境毒素深度排查
头发重金属检测涵盖汞、铅、镉等8项指标。双酚A暴露评估：避免接触购物小票热敏纸，改用玻璃餐具。建立家庭「清洁区」：卧室配备HEPA滤网空气净化器，夜间维持PM2.5<15μg/m³。

生殖功能提升方案
男性精子DNA碎片率（DFI）优化：辅酶Q10 200mg/d+左旋肉碱1000mg/d可使DFI降低40%。女性子宫内膜容受性改善：移植周期前使用粒细胞集落刺激因子（G-CSF）宫腔灌注。建议在生殖中心进行子宫动脉血流检测，PI值需<3.0。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (0, 1, '1500字终极版：系统化备孕工程', '全维度营养计划
制定个性化补充方案：维生素D3/K2复合剂（5000IU+200μg）促进钙定向沉积，Omega-3选择EPA:DHA=3:2的深海鱼油。肠道菌群调理：孕前3月补充特定益生菌株（如罗伊氏乳杆菌DSM17938）。

医疗深度筛查
扩展性携带者筛查（ECS）覆盖200+隐性遗传病，尤其建议犹太裔检测泰萨克斯病、华裔检测地中海贫血。免疫评估包括自然杀伤细胞（NK细胞）活性检测，异常升高者可考虑脂肪乳疗法。

生活环境重构
建立「生育友好型」家居：卧室采用3000K暖光源维持褪黑素分泌，书房配备防辐射窗帘（屏蔽率≥99%）。建议安装全屋净水系统，特别需要去除双酚A（活性炭滤芯）和农药残留（反渗透膜）。

先进监测技术
运用唾液激素检测仪（如Proov）追踪雌激素/孕酮波动。智能备孕手表可连续监测皮肤电导率评估应激水平。家庭用精液分析仪（如YO）提供实时精子质量反馈。

生殖功能强化
女性卵泡期采用「三氧自体血疗法」改善卵巢微循环，黄体期进行子宫内膜搔刮术提升着床率。男性配合低强度体外冲击波治疗（Li-ESWT）改善勃起功能。建议在专业生殖中心进行胚胎着床前基因检测（PGT）模拟演练。

心理社会支持
参加生育力认知行为疗法（CBT）小组，使用VR技术进行分娩情景脱敏训练。企业备孕假制度咨询，了解《女职工劳动保护特别规定》中关于孕前检查的休假权益。建议建立「备孕联盟」社群，共享医疗资源和心理支持。

不同篇幅内容可根据需求组合使用，建议备孕夫妇从200字版快速入门，逐步深入研读1500字系统化方案，必要时咨询生殖医学专家制定个性化计划。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (1, 1, '200字速查版：孕产期核心守则', '营养管理：孕早期每日补充400μg叶酸，中期起增加铁剂（30-60mg/d）预防贫血，钙摄入需达1000mg/d。​体重控制：BMI正常者孕期增重11-16kg，每周增重0.5kg（孕晚期≤0.3kg/周）。​产检节点：NT筛查（11-13周）、唐筛/无创DNA（16-20周）、大排畸（20-24周）、糖耐量（24-28周）。​运动处方：每日30分钟低强度运动（如孕期瑜伽、游泳），避免仰卧位运动超过5分钟。​胎动监测：28周后每日早中晚各1小时计数，2小时内需≥10次。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (1, 1, '500字实用版：分阶段管理要点', '孕早期（0-12周）​
重点预防流产：避免提重物（≤5kg）、禁泡温泉（水温≤38℃）。孕吐严重者可服用维生素B6（10-25mg/次，每日3次），尿酮阳性需静脉补液。叶酸补充需持续至12周，同型半胱氨酸水平应＜8μmol/L。

​孕中期（13-27周）​
营养强化：DHA摄入200mg/d（三文鱼/藻油），锌元素9-11mg/d（牡蛎/南瓜籽）。筛查妊娠糖尿病：OGTT试验空腹血糖＜5.1mmol/L，1小时＜10.0mmol/L，2小时＜8.5mmol/L。出现下肢水肿需排查尿蛋白（24小时尿蛋白定量＜0.3g）。

​孕晚期（28-40周）​
胎位矫正：30周后臀位可尝试膝胸卧位（每日2次，每次15分钟）。监测脐血流S/D值（＜3.0为正常）。准备待产包：必备物品包括胎心监护带、计量型卫生巾、会阴冷敷贴。学习拉玛泽呼吸法，每日练习缩肛运动（凯格尔训练）。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (1, 1, '800字进阶版：全周期管理策略', '营养精准补充
​铁元素：血清铁蛋白＜30μg/L需补充多糖铁复合物（150mg/d），同时服用维生素C（200mg）促进吸收
​钙磷比：最佳比例1.5:1，牛奶饮用不宜超过500ml/日以防磷过量
​血糖调控：GI值＞70的食物需限制，加餐选择核桃（2颗）+无糖酸奶（100g）
​医疗监测体系
​血压管理：家庭自测血压应＜135/85mmHg，尿蛋白肌酐比（PCR）＜0.15
​胎心监护：34周后每周NST检测，加速反应需15分钟内出现2次以上胎心上升15bpm
​生物物理评分：超声评估包含呼吸运动（30秒内≥1次）、胎动（30分钟≥3次）等指标
​并发症预防
​子痫前期预警：sFlt-1/PlGF比值＜38可排除未来1周风险
​ICP筛查：总胆汁酸≥10μmol/L伴皮肤瘙痒需熊去氧胆酸治疗
​GBS防控：35-37周阴道拭子培养阳性者分娩时需青霉素静脉给药');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (1, 1, '1100字专家版：精细化孕产管理', '个性化营养方案
MTHFR基因检测指导叶酸形态：CT/TT基因型需补充5-甲基四氢叶酸（800μg/d）
维生素D3动态监测：孕早期25(OH)D应≥50nmol/L，不足者每日补充4000IU
Omega-3平衡：EPA与DHA比例建议1.5:1，降低早产风险
​医疗技术应用
无创产前检测（NIPT Plus）：可筛查染色体微缺失（如22q11.2缺失综合征）
胎儿纤维连接蛋白（fFN）检测：宫颈分泌物阴性预示7日内分娩概率＜1%
脐动脉多普勒：孕晚期RI值＞0.8提示胎盘功能异常
​产程管理创新
自由体位分娩：采用跪位、侧卧位等姿势加速产程，第二产程可缩短30分钟
镇痛技术选择：硬膜外麻醉维持浓度0.1%罗哌卡因，配合PCEA自控泵给药
延迟断脐：足月儿延迟60秒剪脐带，可增加铁储备30%
​产后修复体系
盆底肌修复：生物反馈治疗从产后6周开始，每周2次，持续12周
腹直肌分离：2指以上需采用神经肌肉电刺激（NMES）+悬吊训练
哺乳期用药：布洛芬、头孢类为L1级安全药物，伪麻黄碱需禁用');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (1, 1, '1500字终极版：系统化孕产管理工程', '全周期营养干预系统
孕早期定制防吐饮食：生姜提取物（250mg/d）+维生素B6（75mg/d）联合方案
中晚期血糖调控模型：采用动态血糖监测（CGM）建立个性化膳食指数
哺乳期营养强化：每日增加500kcal热量，补充碘（250μg/d）+胆碱（550mg/d）
​智能监测技术整合
穿戴设备应用：胎心监护腰带（可连续监测48小时）、智能腹带（监测宫缩频率）
大数据预警系统：整合产检数据+胎动趋势+血压波动，AI预测子痫风险
家庭化监测方案：尿液分析试纸（检测尿蛋白/尿糖）+便携式胎心多普勒
​医疗决策支持体系
早产预防方案：宫颈长度＜25mm者使用黄体酮阴道缓释凝胶（90mg/d）
瘢痕子宫管理：孕晚期超声评估子宫下段厚度≥3.5mm可尝试VBAC
多胎妊娠监控：双绒毛膜双胎需每4周超声评估，TTTS筛查持续至26周
​心理社会支持网络
产前焦虑干预：正念减压疗法（MBSR）每周3次，配合HRV生物反馈训练
产后抑郁筛查：EPDS量表在产后1/6周测评，≥13分需认知行为治疗（CBT）
家庭关系调适：引入伴侣参与的分娩教育课程，建立哺乳期分工协作机制
​产后康复技术集群
内脏复位疗法：采用Visceral Manipulation技术改善子宫脱垂
疤痕修复方案：剖宫产疤痕使用点阵激光（1540nm）+硅酮贴联合治疗
体态重塑工程：三维步态分析制定普拉提训练计划，纠正孕期重心偏移');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (2, 1, '200字速查版：产褥期核心要点', '生理恢复：产后6周内避免重体力劳动，每日观察恶露变化（血性→浆液性→白色，持续4-6周）。​营养重点：产后3天宜流质饮食（小米粥、生化汤），逐步增加优质蛋白（鱼肉/鸡蛋每日150g）。​伤口护理：剖宫产伤口每日碘伏消毒，顺产侧切伤口用高锰酸钾坐浴（1:5000）。​哺乳指导：按需喂养，含乳姿势呈"鼻尖对乳头"，预防乳腺炎。​心理支持：EPDS量表筛查产后抑郁，得分≥10分需专业干预。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (2, 1, '500字实用版：分阶段管理方案', '产后1-3天（急性期）​
子宫复旧：每小时宫底下降1-2cm，产后24小时注射缩宫素10U
排泄管理：6小时内需自主排尿，膀胱残余尿量超声检测＜50ml
疼痛控制：布洛芬400mg每6小时口服，冰敷会阴部每次≤20分钟
​产后4-14天（修复期）​
膳食进阶：麻油猪肝（补铁）+红豆汤（利尿），红糖饮用≤7天
盆底训练：凯格尔运动每日3组，每组10次收缩（持续5秒）
体温监测：每日4次，持续＞38℃提示感染风险
​产后15-42天（巩固期）​
恶露异常识别：血性恶露持续＞2周需排查胎盘残留
产后复查项目：血常规（Hb≥110g/L）、尿常规（蛋白阴性）、盆底肌力评估
避孕指导：哺乳期首选避孕套，月经恢复后可采用曼月乐环');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (2, 1, '800字进阶版：系统化护理体系', '中西医结合调理
​生化汤应用：自然分娩者产后3天服用（当归24g+川芎9g+桃仁6g），促进恶露排出
​药膳食疗：
气血双补：黄芪30g+当归6g炖乌鸡（产后10天起）
通乳组合：王不留行15g+通草10g煮鲫鱼汤
​穴位保健：乳根穴按摩（促进泌乳）、三阴交艾灸（调理内分泌）
​康复训练方案
腹直肌修复：仰卧抬腿训练（每日2组，每组15次）
骨盆矫正：使用骨盆带每日8小时，压力值控制在20-30mmHg
有氧运动：产后6周开始快走训练，心率控制在（220-年龄）×60%
​新生儿同步护理
脐部护理：75%酒精环形消毒至残端脱落，渗血时加压包扎
黄疸监测：经皮胆红素值＞12mg/dl需蓝光治疗
睡眠周期：建立昼夜节律，夜间哺乳间隔逐步延长至3小时');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (2, 1, '1100字专家版：精准医学管理', '病理状态识别
晚期产后出血：产后24小时出血量＞500ml，需排查子宫收缩乏力（宫底高度＞脐上2指）
产褥感染预警：CRP＞10mg/L+中性粒细胞＞80%提示感染
静脉血栓预防：D-二聚体＞3mg/L需低分子肝素（依诺肝素40mg qd）
​哺乳医学支持
泌乳素调控：夜间哺乳间隔≤4小时维持PRL水平
堵奶处理：Lecithin 1200mg每日口服降低乳汁黏稠度
药物安全分级：甲硝唑（L2级）可安全使用，四环素类禁用
​心理干预技术
CBT疗法：纠正"完美母亲"认知偏差，每周2次团体治疗
家庭系统治疗：改善夫妻沟通模式，建立育儿责任分担机制
生物反馈训练：通过HRV监测提升自主神经调节能力
​环境优化标准
居室参数：温度22-24℃，湿度50-60%，PM2.5＜35μg/m³
消毒规范：奶瓶煮沸＞5分钟，吸奶器配件每日紫外线消毒
访客管理：限定每日探视人数≤3人，拒绝呼吸道感染者接触新生儿');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (2, 1, '1500字终极版：全周期管理工程', '智能监测系统
可穿戴设备：
智能腹带：监测宫缩压力及子宫复旧速度
哺乳文胸：实时记录泌乳量及婴儿吸吮力度
大数据预警平台：整合恶露性状照片AI分析、体温曲线异常检测
​个性化营养方案
代谢组学指导：通过尿液有机酸分析定制抗氧化剂补充方案（如谷胱甘肽500mg/d）
微量元素平衡：
锌铜比例维持1:1（牡蛎+黑巧克力组合）
硒摄入量60μg/d（巴西坚果2颗）增强免疫
​康复医学技术集群
筋膜修复：采用Graston技术处理剖宫产疤痕粘连
内脏复位：Visceral Manipulation手法改善产后脏器下垂
疼痛管理：DMS深层肌肉刺激仪治疗腰骶部疼痛
​文化适应性照护
传统习俗科学化：
洗头管理：产后3天起用生姜煮水（40℃），即时吹干
饮食禁忌：忌生冷改为食物中心温度≥60℃
跨文化方案：针对穆斯林产妇定制礼拜体位调整指南
​法律权益保障
职场保护：依据《女职工劳动保护特别规定》落实哺乳假（每日1小时至婴儿1周岁）
医疗纠纷应对：电子病历留存规范（包含产程记录、用药清单）
保险规划：选择覆盖妊娠并发症（如羊水栓塞）的商业保险');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (3, 1, '200字速查版：产后恢复核心要点', '黄金期：产后6周内重点子宫复旧（每日宫底下降1-2cm），42天复查评估盆底肌力。​营养重点：哺乳期每日增加500kcal热量，钙摄入1200mg/d（牛奶500ml+芝麻酱20g）。​伤口护理：剖宫产伤口用医用硅酮贴预防增生，会阴撕裂用康复新液湿敷。​运动禁忌：6周内避免卷腹运动，8周后逐步恢复有氧训练。​心理支持：EPDS量表筛查（≥10分需干预），保证每日连续睡眠≥4小时。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (3, 1, '500字实用版：分阶段恢复方案', '急性期（产后0-7天）​
子宫按摩：顺时针按压宫底，每日3次预防宫缩乏力
排泄管理：凯格尔运动促进排尿（收缩5秒→放松10秒循环）
疼痛控制：布洛芬400mg每8小时口服，冰敷会阴部≤15分钟/次
​修复期（产后8-42天）​
膳食进阶：
第一周：生化汤（当归24g+川芎9g）促进恶露排出
第二周：杜仲猪腰汤（补肝肾）每周3次
第三周：黄芪炖乌鸡（补气血）每周2次
疤痕护理：剖宫产伤口6周后使用脉冲染料激光（585nm）改善增生
​巩固期（产后42-180天）​
盆底肌修复：生物反馈治疗（每周2次，持续6周）
腹直肌分离：2指以上者采用悬吊训练（SET）配合肌内效贴
体态矫正：普拉提球训练纠正骨盆前倾（每日20分钟）');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (3, 1, '800字进阶版：系统化恢复体系', '中西医结合方案
​艾灸疗法：关元穴+气海穴隔姜灸（每周3次），提升基础体温0.3-0.5℃
​药膳配方：
通乳散结：蒲公英15g+夏枯草10g煎服（乳腺炎初期）
祛湿消肿：赤小豆30g+茯苓20g煮水代茶饮
​穴位注射：维生素B12足三里注射（每周1次）改善胃肠功能
​运动康复计划
产后6周：死虫式训练（仰卧屈膝抬腿）激活核心肌群
产后12周：瑞士球臀桥（每日3组×15次）强化臀大肌
产后24周：TRX悬吊训练（每周3次）重建动态稳定性
​皮肤修复技术
妊娠纹治疗：微针（0.5mm深度）联合富血小板血浆（PRP）注射
色素沉淀：皮秒激光（755nm波长）每月1次，共4次疗程
腹部紧致：射频紧肤（温度控制在42-45℃）每周1次');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (3, 1, '1100字专家版：精准医学恢复', '代谢功能重建
甲状腺监控：TSH需维持≤2.5mIU/L，FT4＞12pmol/L
胰岛素抵抗：HOMA-IR指数＞2.5需二甲双胍500mg/d（哺乳期安全）
骨密度恢复：DXA检测腰椎T值＞-1.0，不足者补充维生素K2（180μg/d）
​生殖系统修复
子宫内膜再生：宫腔灌注粒细胞集落刺激因子（G-CSF）每周1次
卵巢功能恢复：DHEA 25mg/d口服（适用于AMH＜1.1ng/ml者）
阴道弹性重建：CO2点阵激光（2940nm）治疗阴道松弛
​精神心理干预
神经反馈训练：通过EEG监测调节前额叶α波（8-12Hz）改善情绪
激素替代方案：经皮雌二醇（50μg/d）用于严重产后抑郁（需停母乳）
家庭系统治疗：建立育儿分工日志，平衡夫妻责任负荷
​并发症防治
静脉血栓：D-二聚体＞0.5mg/L时启用气压治疗（每日2次，每次30分钟）
乳腺脓肿：超声引导下穿刺引流+头孢曲松（2g/d静滴）
耻骨联合分离：骨盆带压力值维持30-40mmHg，结合体外冲击波治疗');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (3, 1, '1500字终极版：智能化恢复工程', '智能监测系统
可穿戴设备：
盆底肌电裤：实时监测Ⅰ/Ⅱ类肌纤维收缩力（目标值＞35μV）
智能腹带：压力反馈调节（20-50mmHg动态适应）
AI健康管家：
恶露性状识别：手机拍照自动分析感染风险
哺乳量计算：通过吸奶器蓝牙传输数据，生成泌乳曲线
​分子级营养方案
基因定制：
MTRR基因A66G位点突变者，需活性叶酸（5-MTHF 800μg/d）
SOD2基因Val16Ala多态性者，增加辅酶Q10 200mg/d抗氧化
代谢组学干预：尿液甲基丙二酸水平＞3mmol/L时补充维生素B12 1000μg/d
​再生医学技术
干细胞治疗：脐带间充质干细胞静脉输注（2×10^6 cells/kg）修复卵巢早衰
富血小板纤维蛋白（PRF）：会阴撕裂处注射促进Ⅲ型胶原再生
3D生物打印：定制化盆底支撑网片修复重度子宫脱垂
​跨学科康复体系
中医五行调理：根据体质检测（舌脉诊+红外热成像）定制药浴方
水疗康复：水下跑步机训练（水温32℃）减轻关节负荷30%
虚拟现实技术：VR场景模拟训练改善产后空间感知障碍
​法律权益矩阵
职场保护：依据《女职工劳动保护规定》落实弹性工作制（哺乳期每日减少1小时）
保险规划：选择覆盖产后并发症（如羊水栓塞后遗症）的高端医疗险
医疗维权：电子病历区块链存证，确保诊疗记录不可篡改');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (4, 1, '200字速查版：0-1岁核心要点', '喂养：新生儿每日哺乳8-12次，4月龄后间隔3-4小时。6月龄添加高铁米粉（每日30g），逐步引入肉泥（红肉优先）。​睡眠：0-3月睡眠总量14-17小时，4-6月建立昼夜节律（夜间连续睡眠≥6小时）。​发育里程碑：3月抬头90°、6月独坐、9月扶站、12月迈步。​疫苗计划：出生24h乙肝+卡介苗，2月龄五联/四联首剂，6月龄手足口疫苗。​安全警示：婴儿床避免柔软物品，辅食添加单种食物观察3天，发热＞38.5℃（＜3月）需急诊。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (4, 1, '500字实用版：分月龄管理指南', '0-3月关键照护
​母乳技巧：C型托乳姿势，有效含乳时下巴紧贴乳房，听到吞咽声（1-3次/秒）
​脐部护理：75%酒精环形消毒至残端脱落（7-14天），渗血时加压包扎
​早教刺激：黑白卡距离20cm追视，沙锤声音寻源训练（每日2次，每次3分钟）
​4-6月过渡期
​辅食添加表：
月龄	食材	质地	频次
6m	高铁米粉	流质	1次
6.5m	红肉泥/南瓜泥	糊状	2次
​大运动训练：拉坐练习（每日5组），俯卧撑手肘离地30秒
​7-12月发展期
​自主进食培养：提供硅胶勺（7m）、手指食物（蒸软西蓝花/8m）、敞口杯（10m）
​语言启蒙：使用拟声词（汪汪/嘀嘀），回应宝宝发声（延迟2秒等待回应）
​安全防护：插座安装保护盖，家具尖角软包，锁定药品柜（误服高锰酸钾需立即喝牛奶中和）');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (4, 1, '800字进阶版：系统化养育方案', '科学喂养体系
​母乳营养监测：乳母每日需摄入DHA 200mg（三文鱼100g/日）、钙1200mg（牛奶500ml+芝麻酱15g）
​配方奶冲调：70℃水温杀灭坂崎肠杆菌，冷却至40℃喂养，剩余奶液存放≤2小时
​过敏防控：高致敏食物（鸡蛋/花生）延迟引入无益，6月后每次添加1/8蛋黄观察反应
​睡眠能力培养
​入睡信号识别：揉眼（中度困意）、眼神呆滞（需立即哄睡）、哭闹（过度疲劳）
​自主入睡阶梯法：
4月龄：引入安抚巾（100%棉质）
6月龄：固定睡前程序（洗澡→抚触→摇篮曲）
9月龄：渐进式撤离法（每3天延长响应间隔2分钟）
​发育评估工具
​ASQ-3筛查表：每月评估沟通（对名字反应）、大运动（独坐30秒）、精细动作（抓握葡萄干）
​预警征象：
6月不能翻身
9月无意识发"mama/baba"音
12月无食指指物
出现任意一条需儿童保健科就诊');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (4, 1, '1100字专家版：精准养育策略', '营养代谢管理
​铁储备监测：6月龄血清铁蛋白＜12μg/L需补充铁剂（2mg/kg/d）
​维生素D定制：
纯母乳喂养：出生后每日400IU
配方奶喂养：根据奶量计算（每升含40IU则补差额）
早产儿：前3月每日800IU
​疾病应对方案
​热性惊厥：侧卧防误吸，记录发作时间（＜5分钟无需用药），禁用掐人中
​湿疹护理：
轻度：每日厚涂保湿霜（成分含神经酰胺）
中度：0.05%地奈德乳膏（激素效价最低）每日2次，连续≤7天
​便秘判断：排便间隔＞3天且粪便呈羊屎状，可给予西梅泥（30g/次）
​心理联结建立
​安全依恋培养：
0-6月：及时响应需求（90秒内）
6-12月：躲猫猫游戏发展物体恒存概念
​社交参照训练：陌生情境中观察父母表情（微笑鼓励/皱眉警示）
​环境安全工程
​空气净化标准：PM2.5＜25μg/m³（配备H13级滤网），甲醛＜0.08mg/m³（持续通风6月）
​声光管理：
白噪音＜50分贝（吸尘器录播音无效）
夜间小夜灯亮度≤25流明（暖光波长＞550nm）');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (4, 1, '1500字终极版：全维度养育体系', '智能监测系统
​智能尿布：
排泄物pH值监测（尿路感染预警：pH＞7.5持续3次）
排便频率AI分析（便秘预测准确率＞85%）
​可穿戴体征衣：
呼吸波形监测（呼吸暂停＞20秒报警）
皮肤温度梯度（躯干-四肢温差＞3℃提示循环不良）
​发育促进技术
​运动能力阶梯

3月：电动旋转床辅助翻身训练（转速5°/秒）
6月：悬浮爬行毯（减重30%助力爬行）
9月：智能扶栏（压力传感器调节支撑力度）
​语言神经激活

双语暴露方案：主要照料者使用母语，次要者用第二语言（每日接触≥30分钟）
脑机接口反馈：通过EEG检测语言中枢活跃度（颞叶β波13-30Hz），调整互动频率
​精准营养工程
​母乳成分分析仪：
脂肪含量监测（理想值3.5-4.5g/dL）
乳糖不耐受筛查（呼气氢浓度＞20ppm时启用低乳糖配方）
​辅食3D打印：
缺铁宝宝：高铁配方（每100g含铁10mg+维生素C 50mg）
过敏体质：水解蛋白米糊（分子量＜1500Da）
​健康预警网络
​基因早筛组合：
SMN1基因检测（脊肌萎缩症携带者筛查）
G6PD缺乏症快速诊断（采足跟血荧光定量法）
​肠道菌群干预：
剖宫产儿：出生后口服益生菌（鼠李糖乳杆菌GG株+动物双歧杆菌BB-12）
抗生素使用期：布拉氏酵母菌（每日250mg）预防腹泻
​文化适应性养育
​传统习俗科学化：
"蜡烛包"改良：使用弹性襁褓（髋关节外展角维持40-60°）
"剃胎发"替代方案：激光测厚仪监控发量（＜1mm²需补充生物素）
​跨文化营养方案：
地区	特色辅食	营养强化重点
北欧	鳕鱼肝油泥	维生素AD
东亚	铁强化海苔粉	碘+铁
非洲	木薯叶泥+花生酱	蛋白质+锌
​家庭支持生态
​父母技能认证：
CPR考核标准：胸外按压深度4cm（婴儿模拟器电子反馈）
呛咳急救法：海姆立克手法成功率监测（压力传感器验证）
​智能育儿中台：
疫苗提醒：自动关联当地接种点库存（缺苗时启动跨区调配）
发育轨迹：生长曲线自动比对WHO标准（＜3rd百分位触发预警）');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (5, 1, '200字速查版：关键要点', '1-2岁是宝宝动作和语言爆发期。每日需保证500ml奶量，逐步过渡到三餐两点，引入软米饭、碎菜肉等固体食物。15个月独走稳，18个月尝试扶栏上下楼梯，24个月可踢球或奔跑。语言发展从单字到短句，18个月会说50个词，24个月能组合主谓短语。睡眠总时长12-14小时，白天保留1次小睡。安全防护重点包括家具防倾倒、电源插座保护和误吞预防（避免直径＜4cm的物件）。疫苗按时接种，18个月强化五联疫苗，24个月接种甲肝首剂。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (5, 1, '500字实用版：分阶段照护指南', '12-18个月重点：此时宝宝进入学步期，需提供防滑袜和宽底学步鞋。饮食从糊状过渡到碎末状，每餐搭配3类食物（如碎鸡肉+胡萝卜丁+软面条）。引入开放式坐便器，观察排尿间隔超过2小时开始如厕训练。玩具选择促进精细动作的嵌套套杯和大颗粒积木，避免细小零件。语言启蒙多用实物命名，如指着苹果清晰重复“这是苹果”。

19-24个月发展：宝宝进入自我意识敏感期，允许有限选择（如“穿红色还是蓝色的衣服？”）减少抵触情绪。饮食中加入需咀嚼的食材，如蒸熟的西蓝花茎秆或苹果薄片。鼓励用短句表达需求，当宝宝说“水”时，示范完整句子“我要喝水”。大运动发展重点包括攀爬矮梯（高度30cm内）和踢球游戏。建立简单规则，如“玩具玩完放回盒子”，用可视化图片提示。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (5, 1, '800字进阶版：系统化发展策略', '营养管理：每日膳食需包含四大类——谷物（100-150g）、蛋白质（鸡蛋1个+肉鱼50g）、蔬菜（100g切碎）、水果（100g去核）。关键营养素关注铁和DHA，每周至少3次红肉（牛肉末或猪肝泥），搭配维生素C丰富的猕猴桃汁促进吸收。奶制品选择全脂巴氏奶或配方奶，避免含糖酸奶。点心时间提供奶酪块或蒸南瓜条，控制甜食频率每周≤3次。

动作能力培养：15个月重点训练蹲起捡物，用矮凳放置玩具引导屈髋动作。18个月引入平衡木游戏（宽30cm的木板），家长牵手侧行增强本体感。24个月可玩投掷沙包（重量100g内），目标距离从50cm逐步增至1米。精细动作从大孔串珠过渡到拼插积木，每日练习5分钟握笔涂鸦，选择粗杆三角蜡笔。

语言社交发展：采用“扩展对话”技巧，当宝宝指车说“车”，回应“对的，红色小汽车跑得快”。每日亲子阅读20分钟，选择触觉书或翻翻书增强互动。社交游戏从平行玩耍（各玩各的）转向简单合作，如共同推球或传递积木。情绪管理通过命名感受实现，当宝宝哭闹时说“你生气了，因为饼干掉了”，再提供解决方案。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (5, 1, '1100字专家版：精准养育方案', '认知神经开发：利用物体恒存概念设计游戏，将玩具藏在半透明纱布下引导寻找。空间认知通过整理玩具强化，指定“汽车停放在蓝色框里，积木回到木箱”。因果关系学习使用按压弹出玩具，选择需两次操作才能弹起的进阶款式。18个月引入形状分类盒，从圆形孔开始，逐步加入星形和不规则孔洞。

睡眠科学管理：建立固定睡前程序：洗澡（水温38℃）-抚触（10分钟）-摇篮曲（同一首）。卧室环境保持全黑（遮光帘遮光率＞95%），使用红色夜灯（波长620nm）减少褪黑素抑制。若夜醒频繁，采用渐进响应延迟法：首晚等待5分钟再安抚，逐日增加2分钟。白天小睡避免超过下午3点，防止昼夜节律紊乱。

疾病预防体系：疱疹性咽峡炎护理用低温流食（如冷藏酸奶）缓解咽痛，禁用刺激性食物。腹泻期间补充锌剂（20mg/d，连服10天）促进肠黏膜修复。过敏预防重点筛查鸡蛋和坚果，首次尝试时给予微量（1/8蛋黄），观察48小时反应。家庭急救包需配备生理海盐水（处理鼻腔异物）和网状牙胶（防咬伤舌部）。

安全工程升级：家具固定使用L型支架，电视柜加装防倾倒链。楼梯口安装垂直栏杆防护门（间隙＜6cm），窗户设置限位器（开缝＜10cm）。厨房设置安全屏障，刀具存放高度＞1.5米。误服应急措施需牢记：强酸强碱禁止催吐，立即饮用牛奶100ml中和，携带包装迅速就医。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (5, 1, '1500字终极版：全维度养育体系', '智能监测技术：可穿戴步态分析袜通过足底压力传感器，实时监测内八/外八步态（偏差角＞15°时预警）。语言记录项圈自动统计每日新词汇量，当18个月低于30词或24个月未出现双词短语时触发提醒。智能餐盘通过图像识别分析营养摄入，自动生成钙铁锌摄入报告并与推荐值对比。

个性化营养方案：基因检测指导乳糖耐受策略，LCT基因突变者采用低乳糖配方奶+乳糖酶制剂。脂肪酸代谢分析定制DHA补充方案，APOE ε4基因携带者提高EPA比例至2:1。功能性零食开发冻干草莓夹心海苔，强化铁含量至每份3mg，搭配针叶樱桃粉提供天然维生素C。

脑科学启智工程：脑电生物反馈训练每周3次，通过抑制θ波（4-7Hz）提升专注力，增强γ波（30-100Hz）促进语言处理速度。跨感官整合训练使用温感触摸屏，让宝宝在听到“热”时触摸红色区域（40℃模拟），听到“冷”时接触蓝色区域（18℃模拟）。

文化适应性养育：亚洲家庭可延续米食传统，将高铁米粉混入粥品，搭配芝麻酱补充钙质。欧洲风格引入手指食物时，选择奶酪条（低钠版）和全麦面包棍。热带地区注重补水，用椰子水冰块（硅胶模具制作）替代普通冷饮，补充电解质同时锻炼抓握。

家庭生态优化：空气净化系统维持PM2.5＜10μg/m³，配备VOC传感器实时监测甲醛（＜0.08mg/m³）。游戏区铺设模块化地垫（厚度2cm），边缘采用弧形设计防磕碰。智能温控餐椅分区域加热，主食区保持60℃保温，水果区恒定40℃保留营养。

法律权益网络：依据《未成年人保护法》，公共场所需配备尿布台（承重＞15kg）和哺乳室（面积≥3㎡）。企业须为父母提供弹性工时，每周至少1天远程办公。医疗数据通过区块链存储，疫苗记录和生长曲线加密上链，确保隐私与可追溯性。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (6, 1, '200字速查版：核心要点', '2-3岁是宝宝自主意识和社交能力飞跃期。每日饮食需包含四大类：谷物（150g）、蛋白质（鸡蛋+肉鱼70g）、蔬菜水果各150g。语言发展从短语到完整句子，30个月能回答简单问题。大运动重点在平衡能力（单脚站3秒）和协调性（双脚跳）。社交启蒙强调轮流玩玩具和表达情绪词汇（如“我生气了”）。如厕训练宜在夏季开始，选择开放式马桶圈。安全防控需警惕误吞纽扣电池（立即送医）、烫伤急救（冷水冲15分钟）。定期评估身高体重曲线（每年增长7cm/2kg）。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (6, 1, '500字实用版：分阶段养育指南', '24-30个月重点：

​语言爆发期：通过“扩展对话”引导完整表达，当宝宝说“车跑”，回应“红色小汽车在马路上跑得很快”。每日亲子阅读20分钟，选择情节简单的绘本（如《好饿的毛毛虫》）。
​精细动作：练习穿大孔珠子（直径1cm）、拧瓶盖（从大口径到小口径）。提供可水洗蜡笔涂鸦，鼓励画闭合圆圈。
​社交规则：用“轮流沙漏”（3分钟计时）学习等待，玩角色扮演游戏（医生/厨师）理解社会分工。
31-36个月发展：

​逻辑思维：玩三块拼图（切割为直角、弧线等形状），理解“大小”“上下”等空间概念。通过分类游戏区分水果和蔬菜。
​情绪管理：命名复杂情绪（失望、嫉妒），提供“冷静角”放软垫和毛绒玩具。建立日常惯例表（图片版）减少抵触。
​自理能力：学习穿脱套头衫（先认领口标签）、用安全餐刀切香蕉片。如厕后自主冲水、洗手（用七步法贴图提示）。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (6, 1, '800字进阶版：系统化养育策略', '认知能力训练：

​记忆游戏：藏起3件玩具让宝宝回忆，逐步增至5件。用卡片匹配游戏强化短期记忆（从3对到6对）。
​逻辑推理：玩“如果…就…”情景问答（“如果下雨了，我们需要什么？”）。通过实物比较理解“轻重”“粗细”（棉花与石头的触感对比）。
​数学启蒙：数楼梯台阶（1-10）、分饼干练习“一半”概念。用积木塔理解“3比2高”。
健康管理要点：

​营养定制：挑食宝宝采用“彩虹餐盘法”，每餐保证3种颜色食物（西红柿的红、菠菜的绿、南瓜的黄）。补铁优选动物肝脏（每周30g猪肝泥），搭配猕猴桃促进吸收。控制果汁摄入（每日＜120ml），以果泥代替保留膳食纤维。
​睡眠科学：维持12-13小时总睡眠，建立“星空投影仪+白噪音”入睡仪式。若夜惊频繁，检查卧室温度（20-22℃）和湿度（50-60%）。
​疾病预防：手足口病流行期避免游乐场海洋球池，接种EV71疫苗。过敏性鼻炎患儿每日生理盐水洗鼻，监测花粉浓度（＞50粒/m³时减少外出）。
安全防护升级：

厨房设置安全锁，刀具存放高度＞1.5米。选择圆角家具，硬质桌角贴缓冲胶条。
乘车必须使用后向式安全座椅（体重＜18kg），教会“不能碰方向盘”规则。
防走失训练：熟记父母姓名电话，模拟商场迷路场景演练“找穿制服的工作人员”。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (6, 1, '1100字专家版：精准发展方案', '语言发育筛查：

30个月需掌握250个词汇，能组合4-5字句子（如“妈妈抱我看车车”）。若出现代词混淆（你/我）或语法错乱，需语言治疗师评估。
双语家庭实施“一人一语”原则（父母分别使用不同语言），避免混合输入导致表达混乱。
运动能力评估：

平衡测试：24个月可走15cm宽平衡木（辅助），36个月独立完成。单脚站立标准：30个月≥3秒，36个月≥6秒。
球类技能：30个月双手接大球（直径25cm），36个月能用脚踢球命中1米宽目标。
心理社会支持：

​自主意识引导：提供有限选择（“先刷牙还是先换睡衣？”），用“当…然后…”句式建立因果关系（“当玩具收拾好，然后我们可以出去玩”）。
​恐惧应对：对黑暗恐惧使用渐进暴露法：先开夜灯讲故事，逐步过渡到全黑环境听轻音乐。对医疗检查恐惧，用玩具听诊器角色扮演降低焦虑。
环境优化工程：

学习区设置：护眼灯色温4000K，照度500lux。玩具柜分格标签用图片标识（积木区贴积木照片）。
空气质量：PM2.5维持＜35μg/m³，新家具甲醛释放量需＜0.08mg/m³。春季花粉季启用新风系统（换气量＞150m³/h）。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (6, 1, '1500字终极版：全维度养育体系', '智能监测技术：

可穿戴语言分析仪实时追踪词汇多样性指数（30个月目标值＞0.7），预警语言发育迟缓。足压感应鞋垫监测步态对称性（左右脚压力差＜15%）。
AI营养管家通过餐盘图像识别，分析蛋白质摄入达标率（每日＞80%生成笑脸贴纸奖励）。
个性化发展计划：

​基因导向营养：
TAS2R38苦味受体基因检测，指导蔬菜选择（敏感型宝宝优选甜味蔬菜如胡萝卜）。
FTO肥胖基因携带者，定制低GI食谱（糙米替代白米，零食用鹰嘴豆泥替代饼干）。
​神经发育干预：
触觉过敏宝宝用刷肤疗法（Wilbarger Protocol），每日3次各2分钟。
注意力缺陷者进行“找不同”游戏（从3处到5处差异），配合脑电生物反馈训练。
跨学科能力培养：

​STEAM启蒙：
科学：浮沉实验（树叶与石头的密度对比）。
工程：用大颗粒积木搭建斜坡（角度从15°到30°）。
艺术：指纹画与自然材料拼贴（树叶/石子）。
​生态教育：
设立“植物观察角”，记录绿豆发芽日记（每日测量高度）。
实践垃圾分类（可回收/其他垃圾），用磁贴板模拟操作。
家庭支持系统：

​父母学堂：
正念养育课程：每日10分钟呼吸练习，降低养育压力（心率变异性提升＞10%）。
急救认证：通过婴儿CPR模拟考核（按压深度4cm，频率100-120次/分）。
​智能家居改造：
防夹手门安装压力感应器（响应时间0.1秒）。
温控水龙头预设38℃防烫伤，出水量＜2L/分钟。
法律与文化适配：

依据《学前教育法》，社区需配备免费亲子活动中心（每周开放＞20小时）。公共场所母婴室标配AED急救设备。
传统文化融合：清明节用艾草拓印画讲解自然轮回，中秋节玩“月饼分半”游戏理解分数概念。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (7, 1, '200字速查版：核心要点', '3-5岁是儿童认知与社交能力飞跃期。每日需保证10-13小时睡眠，膳食包含五大类（谷物150g+肉蛋60g+蔬菜200g+水果200g+奶类500ml）。语言发展目标：4岁能用复合句叙述事件，5岁理解反义词和幽默。精细动作重点在握笔（三角蜡笔）、系大扣子。大运动需掌握单脚跳、接球。社交规则学习轮流、分享和道歉。安全教育涵盖身体隐私（“泳衣覆盖处不许碰”）、紧急求助（熟记家庭地址）。建议每年视力筛查，每日户外活动≥2小时。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (7, 1, '500字实用版：分龄发展策略', '3-4岁重点：

​语言表达：通过“故事接龙”游戏扩展叙事能力，家长起头“从前有只小熊…”，孩子续编情节。
​数学启蒙：用实物操作理解数量（5以内点数），配对袜子感知“相同”与“不同”。
​情绪管理：制作“情绪天气表”，用太阳代表开心、乌云代表生气，每日睡前回顾感受。
4-5岁进阶：

​逻辑思维：玩因果关系推理（“如果一直下雨，花园会怎样？”），通过植物生长观察理解时间顺序。
​自理能力：制定晨间流程表（图片版）：穿衣→刷牙→整理书包，逐步减少代劳。
​社交技巧：角色扮演“冲突解决”，示范说“我可以玩这个玩具吗？”替代抢夺行为。
健康管理：

每年测量骨龄（身高增长＜5cm/年需排查生长激素）。
蛀牙预防使用含氟牙膏（豌豆大小），每半年涂氟。
建立“身体红绿灯”概念：绿色部位可示人（手脸），红色部位需保护（生殖器官）。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (7, 1, '800字进阶版：系统化成长方案', '认知能力开发：

​注意力训练：从“听指令做动作”（摸左耳+抬右脚）过渡到舒尔特方格（5×5数字矩阵）。
​记忆力提升：玩“购物清单”游戏（记忆5件物品），逐步增至10件；通过绘本复述锻炼细节回忆。
​创造力培养：提供开放性材料（纸箱、布料）进行主题搭建（如“外星基地”），避免模板化手工。
情感与社会性：

​同理心建立：阅读时提问“如果你是主人公会怎么办？”，参观动物救助站感知生命关怀。
​挫折教育：将失败重构为学习机会，拼图卡住时说“这块有点难，我们试试转方向”。
​性别意识：用科学绘本解释身体差异，强调“男孩女孩都能当医生或护士”。
学习习惯奠基：

设立“学习角”：定时器设定15分钟专注时段，完成后奖励自由活动。
前书写准备：在沙盘上画波浪线→迷宫游戏→临摹简单汉字（如“口”“田”）。
时间概念：制作星期盘（周一至周日图标），每天移动指针感知周期循环。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (7, 1, '1100字专家版：精准教育方案', '语言与阅读发展：

​语音意识训练：通过押韵游戏（“找出和‘猫’押韵的词”）提升音素敏感度，4岁半引入拼音符号。
​阅读理解策略：
预测：封面猜测故事内容
链接：联系自身经历（“你像主角一样勇敢吗？”）
总结：用“谁+做了什么+结果”复述框架
​双语启蒙：采用“时间段分隔法”，上午全中文环境，下午沉浸式英语（每天累计1小时）。
科学思维培养：

​观察实验：种植豆芽记录生长（每日测量高度+绘图），对比光照/黑暗组差异。
​测量技能：用脚掌丈量房间长度，引入标准单位（“你的脚长15cm，沙发有6只脚长，所以是90cm”）。
​因果推理：搭建斜坡改变小球滚动速度，理解“角度越大→速度越快”。
艺术与审美：

音乐素养：听辨乐器声音（钢琴VS小提琴），用身体节奏回应不同曲风（进行曲拍腿、圆舞曲转圈）。
美术表达：尝试湿画法（水彩扩散效果）、撕贴画（锻炼手部控制）。
戏剧表演：改编经典故事（如《三只小猪》现代版），自制简易道具（纸箱盔甲、布条披风）。
健康管理系统：

体态矫正：筛查“W坐姿”（易致髋内旋），改用盘腿坐或椅子坐。
睡眠监测：穿戴设备检测深睡比例（目标＞20%），调整睡前活动强度。
营养强化：每周2次深海鱼补充DHA，挑食儿童采用“一口规则”（新食物至少尝一口）。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (7, 1, '1500字终极版：全维度成长体系', '脑科学养育实践：

前额叶发育促进：
执行功能游戏：西蒙指令（按颜色拍手）、反向动作（听到“摸头”实际摸脚）
延迟满足训练：积攒贴纸兑换周末公园游，从等待1天逐步延至1周
杏仁核情绪调节：
恐惧脱敏：分阶段接触害怕事物（如怕狗先看图片→观察活体→尝试抚摸）
正念呼吸法：用“闻花香（吸气）-吹蜡烛（呼气）”具象化引导
个性化学习路径：

​学习风格诊断：
视觉型：多用思维导图记忆恐龙分类
动觉型：通过跳数字地板游戏学加减
听觉型：创编乘法口诀歌曲
​天赋识别系统：
空间智能：三维拼图完成速度与准确率分析
自然观察智能：动植物分类能力评估
人际智能：团体游戏中领导力表现追踪
智能环境构建：

可穿戴设备：
脑电波头带监测专注度（β波占比），实时调整任务难度
智能手环记录运动量（每日目标12000步），震动提醒久坐
家居改造：
重力感应书架：常用书籍放置触手可及高度
AR互动墙：点击虚拟按钮触发地理/历史知识讲解
文化与社会适应：

传统节日深化：
清明制作家谱树（理解代际关系）
中秋测量月球坑（链接天文知识）
社会角色体验：
超市采购实践（预算10元选择健康食品）
社区义工活动（给老人读报培养共情）
家庭支持生态系统：

父母成长学院：
情绪急救课程：识别教养焦虑（心率＞100次/分时启动深呼吸）
游戏力认证：掌握20种亲子互动模板（如“打败生气怪兽”枕头大战）
兄弟姐妹关系：
冲突调解四步法：描述问题→表达感受→头脑风暴→选择方案
合作任务设计：共同照顾盆栽（年长者浇水、年幼者松土）
未来能力奠基工程：

财商启蒙：
零钱分配三罐法（储蓄50%+消费30%+捐赠20%）
开设虚拟银行账户，可视化利息增长
科技素养：
简易编程（ScratchJr制作会走动的角色）
机器人拆装（认识传感器与动力系统）');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (8, 1, '200字速查版：核心要点', '5-10岁是学习能力与社会性发展的关键期。每日保证9-11小时睡眠，饮食均衡（谷物200g+肉蛋80g+蔬菜300g）。学习上注重习惯培养：固定作业时间（低年级30分钟/天）、错题本整理。社交重点在解决冲突（用“我信息”表达感受）和团队合作（分组完成家务）。健康管理包括每年视力检查（每日户外≥1小时）、疫苗接种（如HPV 9-14岁首剂）。安全教育强调网络隐私（不透露家庭地址）、身体界限（拒绝不当接触）。兴趣培养每周2-3次专项活动（运动/艺术/科技），避免过度安排。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (8, 1, '500字实用版：分阶段养育策略', '学习能力培养：

​低年级（5-7岁）​：通过数豆子理解加减法，用故事复述提升语言逻辑。建立“学习仪式感”（固定书桌+计时器）。
​中高年级（8-10岁）​：思维导图整理知识点，科学实验培养观察力（如酸碱变色反应）。引入“番茄工作法”（25分钟专注+5分钟休息）。
健康管理要点：

饮食：早餐必备蛋白质（鸡蛋/牛奶），零食替换为坚果（每日15g）和水果。控制屏幕时间（＜2小时/天），每20分钟远眺6米外。
运动：每周3次中高强度活动（跳绳/游泳），穿插平衡训练（单脚闭眼站＞10秒）。
社交与心理：

情绪日记：记录每日开心/烦恼事件，周末家庭会议讨论解决方案。
零花钱管理：分配储蓄（50%）、消费（30%）、公益（20%），通过家务赚取额外收入。
抗挫训练：将失败转化为“学习勋章”（如算错5题换1枚贴纸集奖）。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (8, 1, '800字进阶版：系统化成长方案', '学科能力提升：

​数学思维：用数独（4×4到9×9进阶）培养逻辑推理，实物测量（房间面积/水杯容积）理解几何概念。
​阅读深度：
文学类：角色扮演理解人物动机（“如果你是主角会怎么做？”）
科普类：制作知识卡片（3个事实+1个疑问）
​写作技巧：五感描写法（视觉/听觉/触觉细节），通过家庭菜谱编写练习步骤化表达。
心理健康维护：

焦虑缓解：深呼吸练习（吸气4秒-屏气4秒-呼气6秒），压力球捏握释放紧张。
自我认知：绘制“能力雷达图”（学习/运动/艺术等维度），每季度更新见证进步。
家庭沟通：设立“无评判时间”（每天15分钟自由表达），用共情句式反馈（“听起来你觉得…”）。
科技素养培养：

编程启蒙：Scratch制作互动故事（事件触发-角色运动），理解算法基础。
信息筛选：辨别网络谣言（查证.gov/.edu域名来源），设置安全搜索过滤器。
数字创作：用平板拍摄微电影（脚本-拍摄-剪辑），学习版权基本知识。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (8, 1, '1100字专家版：精准发展策略', '认知神经开发：

执行功能训练：
工作记忆：数字倒背（从3位数到7位数）
认知灵活：词语分类游戏（按颜色/用途/首字母多维度切换）
批判性思维：辩论简单议题（“宠物是否应穿衣服”），收集正反方证据。
个性化教育方案：

学习风格诊断：
视觉型：用思维导图记忆古诗意象
听觉型：录制知识点音频循环播放
动觉型：化学元素周期表跳格子游戏
天赋识别：通过加德纳多元智能测评，针对性强化自然观察（标本制作）或人际智能（小组项目领导）。
健康管理系统：

脊柱保护：书包重量＜体重10%，写姿保持“三个90度”（肘/髋/膝）。
免疫增强：补充维生素D（600IU/天），流感季前接种疫苗（10月最佳）。
性教育：用科学绘本解释生理变化（《乳房的故事》《小鸡鸡的故事》），建立身体自主权意识。
社会适应训练：

冲突解决四步法：
冷静（数到10再回应）
描述事实（不带评判）
表达需求（“我需要轮流玩”）
协商方案（设定计时器各玩5分钟）
社区参与：义卖自制手工（利润捐赠）、参与垃圾分类督导，培养公民意识。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (8, 1, '1500字终极版：全维度发展体系', '学术能力深化工程：

​数学高阶思维：
建模思维：用乐高积木演示分数运算（1/2块+1/4块=3/4块）
财商实践：模拟股票游戏（跟踪3支虚拟股周波动）理解百分比
​语言大师计划：
古文启蒙：用《声律启蒙》玩对联接龙（“云对雨，雪对风”）
外语沉浸：每周1天“英语日”（仅用英语交流简单需求）
​科学探究系统：
提出假设：厨房实验验证“酵母发酵最佳温度”（25℃/35℃/45℃对比）
数据分析：用温度传感器记录数据，制作折线图汇报结论
身心健康生态系统：

​营养基因组学：
FTO基因检测：肥胖高风险者定制低GI食谱（燕麦替代白粥）
CYP1A2咖啡因代谢型：慢代谢者禁用含咖啡因食品
​运动生理优化：
骨龄监测：每年手腕X光评估生长潜力，调整运动强度
体能定制：爆发力型（短跑/跳远）与耐力型（游泳/骑行）区分训练
科技赋能教育：

智能学习工具：
AI作文批改：语法纠错+情感分析（检测积极词汇占比）
VR地理课：虚拟环游世界地标（金字塔/大峡谷）
数字安全防护：
家庭网络设置“儿童模式”（22点自动断网）
社交媒体使用契约（不传照片/不加陌生人/每周使用时长）
文化与社会融合：

传统文化浸润：
节气厨房：清明做青团（艾草汁和面）、冬至包饺子
非遗体验：参观扎染作坊制作方巾，理解化学固色原理
全球视野拓展：
国际笔友计划：每月交换手写信件（附家乡风光明信片）
模拟联合国：代表不同国家辩论环保议题
家庭支持网络：

父母成长体系：
情绪急救认证：识别抑郁前兆（食欲骤变/兴趣丧失）
沟通大师课：非暴力沟通四要素（观察-感受-需要-请求）
兄弟姐妹动态：
合作挑战：共同完成1000片拼图（分配边缘/颜色区块）
竞争机制：家务积分赛（擦桌2分/洗碗5分）兑换周末电影票
未来能力奠基：

创新孵化计划：
专利申请启蒙：改进日常用品（防泼洒水杯设计）
商业思维：柠檬汁摊位实践（成本核算-定价策略-利润分析）
生态责任培养：
家庭碳足迹计算（用电/交通/饮食），制定减排目标
参与濒危动物领养计划（定期收到保护区进展报告）');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (9, 1, '200字速查版：核心要点', '10-15岁是青春期身心巨变期。每日保证9小时睡眠，饮食注重钙（1000mg/天）和铁（男11mg/女15mg）。心理支持需建立开放对话（每日15分钟专注倾听），尊重隐私但监控网络使用（设立屏幕使用契约）。学业管理强调时间规划（四象限法则），错题本升级为电子知识图谱。身体发育指导：女生初潮教育（提前备应急包）、男生变声期声带保护。社交重点培养数字公民意识（不转发未核实信息），鼓励参与社团发展领导力。每年体检关注脊柱侧弯（前屈测试）、视力（验光+眼轴测量）。');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (9, 1, '500字实用版：分领域养育策略', '生理变化应对：

女生：乳房发育期（Tanner II-III阶段）选择无钢圈运动内衣，经期管理APP记录周期，痛经热敷+布洛芬（10mg/kg）
男生：指导剃须技巧（顺毛发生方向），关注睾丸发育不对称（超声排查精索静脉曲张）
共同问题：痤疮护理（含2%水杨酸洁面+非致痘防晒），每年测量骨龄（身高增长＜4cm/年需内分泌科评估）
学业与时间管理：

番茄钟进阶法：45分钟专注+15分钟运动休息（跳绳/拉伸），周末进行知识复盘（费曼学习法）
电子错题本：用Notability扫描错题，分类标签（概念模糊/计算错误），关联相似题型
升学规划：7年级探索职业兴趣（霍兰德测试），8年级制定学科优势发展路径
心理健康维护：

情绪日记升级：用情绪轮盘识别复杂感受（从6种基础情绪扩展到32种）
压力释放：正念呼吸（4-7-8法则：吸气4秒-屏息7秒-呼气8秒），艺术疗愈（曼陀罗绘画/黏土塑形）
危机预警信号：连续两周失眠/食欲骤变/自伤倾向，需立即启动心理干预');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (9, 1, '800字进阶版：系统化成长方案', '健康管理工程：

​营养定制：
体育特长生增加蛋白质（1.5g/kg体重），素食者补充维生素B12（500μg/周舌下含片）
预防缺铁性贫血：每周摄入动物肝脏100g+维生素C 100mg（促进吸收）
​运动处方：
预防脊柱问题：每天靠墙站立5分钟（后脑勺/肩胛骨/臀部贴墙）
增强骨密度：每周3次冲击性运动（篮球/跳绳＞10分钟）
​科技健康：
使用智能手环监测深度睡眠（占比＞20%为佳），REM期不足时调整睡前光照（禁用蓝光＞1小时）
亲子沟通升级：

非暴力沟通四步法：观察行为→表达感受→说明需求→提出请求
（例：“看到你熬夜到凌晨（观察），我担心影响健康（感受），希望保持作息规律（需求），能否11点前交手机？（请求）”）
重大议题对话：
性教育：用《青春期：男孩女孩的身体密码》图解生理变化，强调知情同意权
金钱观：开设联名账户（每月定额零花钱+家务奖励），实践基础理财（储蓄/基金定投）
数字素养培养：

信息批判训练：
识别AI生成内容（检查手指细节/光影逻辑）
使用FactCheck.org验证热点事件
网络安全防御：
双因素认证所有账户，定期更新密码（8位以上混合字符）
模拟网络钓鱼测试（伪造中奖信息），提升风险识别力');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (9, 1, '1100字专家版：精准发展策略', '学业能力深化：

​批判性思维训练：
Socratic Seminar讨论范式：针对社会议题（如AI伦理），担任引导者/质疑者/总结者角色
研究论文写作：从知乎问答提炼观点→知网文献综述→设计问卷调查（样本量＞100）
​学科跨界整合：
历史+地理：绘制“丝绸之路”经济文化影响图谱（标注物产/宗教/技术传播）
物理+艺术：用Chladni图形实验（撒沙观察声波振动）理解频率与形态关系
心理危机干预：

抑郁倾向筛查：PHQ-9量表每月自评（得分＞10分启动CBT认知行为疗法）
社交焦虑缓解：
渐进暴露疗法：从线上文字交流→语音通话→小型聚会发言
社交脚本训练：预设5种常见场景应答方案（如被嘲笑/拒绝他人）
身份认同支持：
LGBTQ+群体：提供PFLAG组织资源，强调“自我接纳比他人认可更重要”
文化认同冲突：通过家族口述史记录（祖辈移民故事）构建多元身份认知
生理健康管理：

内分泌监测：
女生：多囊卵巢综合征筛查（月经周期＞35天+痤疮+脱发三联征）
男生：青春期乳房发育（约50%出现）超声鉴别病理性/生理性
运动损伤预防：
ACL保护训练：单腿跳箱（高度20cm→40cm渐进）强化膝关节稳定性
脊柱侧弯矫正：Schroth三维呼吸训练（每日20分钟针对性肌群激活）');

INSERT INTO parenting_encyclopedia (stage, user_id, title, content)
VALUES (9, 1, '1500字终极版：全维度成长体系', '认知神经开发计划：

高阶执行功能训练：
双重任务处理：边听播客边整理要点（提升注意力分配）
延迟满足实验：选择现在获得50元或一周后获得60元（培养跨期决策力）
脑科学学习法：
记忆宫殿法：用学校走廊定位存储历史事件时间线
间隔重复系统：Anki卡片按记忆曲线（1/7/16天间隔）复习薄弱知识点
个性化成长路径：

​多元智能评估：
自然观察智能：参与公民科学项目（如eBird鸟类追踪）
存在智能探索：哲学辩论“自由意志是否存在”（参考萨特vs斯金纳理论）
​天赋孵化系统：
科技特长：组队参加FRC机器人竞赛（机械设计/编程/商业策划全流程）
艺术专精：申请青年艺术家驻留计划（美术馆提供创作空间+导师指导）
社会适应能力矩阵：

全球胜任力培养：
模拟联合国：代表小国立场撰写气候提案（学习谈判妥协技巧）
跨文化沟通：通过HelloTalk与海外同龄人语言交换（每周2次30分钟）
法律意识构建：
未成年人保护法：情景模拟网络暴力/肖像权侵犯的维权流程
民法实践：签订“学习目标承诺书”（权利义务对等原则启蒙）
健康科技融合：

生物数据追踪：
持续血糖监测（CGM）：优化饮食结构（稳定血糖波动＜3mmol/L）
基因检测指导：
APOE基因型（阿尔茨海默风险）定制脑力训练方案
CYP2C19代谢型（抗抑郁药效预测）辅助用药决策
心理健康科技：
VR暴露疗法：社交恐惧症患者模拟演讲场景（从5人观众逐步增至100人）
情感计算AI：通过语音语调分析抑郁倾向（准确率＞85%）
家庭支持生态系统：

代际沟通机制：
“未来之夜”活动：每月一次角色互换（孩子讲解新技术/父母分享职场经验）
家族数字遗产：共同创建云端记忆库（老照片修复+口述史转录）
危机响应网络：
签订心理急救协议：约定情绪崩溃时的暗号（如“我需要红色警报”）
建立校外导师制：链接行业专家（每月1次职业访谈）');

-- 向 user 表添加 role 列，数据类型为 varchar
ALTER TABLE user
    ADD COLUMN role VARCHAR(255);

-- 向 user 表添加 is_delete 列，数据类型为 varchar，默认值为 '0'
ALTER TABLE user
    ADD COLUMN is_delete VARCHAR(1) DEFAULT '0';

INSERT INTO user (name, password, avatar, phone, role, gender)
VALUES ('test', '123456', 'test', 'test', 'admin', '男');

CREATE TABLE product (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         category_id INT,
                         name VARCHAR(255),
                         subtitle VARCHAR(255),
                         main_image VARCHAR(255),
                         sub_images TEXT,
                         detail TEXT,
                         price DECIMAL(10, 2),
                         stock INT,
                         status INT,
                         create_time DATETIME default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
                         update_time DATETIME default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

CREATE TABLE category (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          parent_id INT,
                          name VARCHAR(255),
                          status INT,
                          create_time DATETIME default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
                          update_time DATETIME default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

INSERT INTO category (name, status, parent_id)
VALUES
    ('卫生护理', 0, 0),
    ('药品', 0, 0),
    ('可穿戴健康监测设备', 0, 0),
    ('保健产品', 0, 0),
    ('健康医疗器械', 0, 0);


-- 创建 order 表
CREATE TABLE `orders` (
    -- 自增主键
                          `id` INT AUTO_INCREMENT PRIMARY KEY,
    -- 订单号，唯一标识订单
                          `order_no` VARCHAR(255) NOT NULL,
    -- 用户 ID，关联用户表
                          `user_id` INT NOT NULL,
    -- 用户地址表 ID，关联用户地址表
                          `user_address_id` INT NOT NULL,
    -- 订单总价，使用 DECIMAL 类型存储精确的小数
                          `payment` DECIMAL(10, 2) NOT NULL,
    -- 支付类型，用整数表示不同的支付方式
                          `payment_type` INT NOT NULL,
    -- 订单状态，用整数表示不同的订单状态
                          `status` INT NOT NULL,
    -- 支付时间
                          `payment_time` DATETIME,
    -- 寄出时间
                          `send_time` DATETIME,
    -- 结束时间
                          `end_time` DATETIME,
    -- 关闭时间
                          `close_time` DATETIME,
                          `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
);

CREATE TABLE `user_address` (
                                `id` INT AUTO_INCREMENT PRIMARY KEY,
                                `user_id` INT NOT NULL,
                                `receiver_name` VARCHAR(255) NOT NULL,
                                `receiver_phone` VARCHAR(20) NOT NULL,
                                `receiver_province` VARCHAR(255) NOT NULL,
                                `receiver_city` VARCHAR(255) NOT NULL,
                                `receiver_district` VARCHAR(255) NOT NULL,
                                `receiver_address` VARCHAR(255) NOT NULL,
                                `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
);
CREATE TABLE message (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         from_id BIGINT NOT NULL COMMENT '发送消息用户',
                         to_id BIGINT NOT NULL COMMENT '接收消息用户',
                         topic_id BIGINT COMMENT '通知主题ID',
                         content VARCHAR(1000) NOT NULL COMMENT '消息内容',
                         is_read INT DEFAULT 0 COMMENT '是否已读，0表示未读，1表示已读',
                         create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '站内通知消息表';

INSERT INTO permission (name)
VALUES ('read - only'), ('write - only'), ('read - write');

INSERT INTO role (name)
VALUES ('admin'), ('user'), ('guest');

INSERT INTO role_permission (role_id, permission_id)
VALUES (1, 3), (2, 1), (3, 2);

CREATE TABLE left_hand (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           kid_id BIGINT,
                           flexibility VARCHAR(255), -- 对应灵活性
                           joint_swelling VARCHAR(255), -- 对应指关节肿胀
                           two_point_discrimination VARCHAR(255), -- 对应两点辨别觉
                           nail_bed_circulation VARCHAR(255), -- 对应甲床循环
                           recommendation TEXT, -- 存储推荐字符串
                           create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    -- 假设 kid 表的主键是 id，这里添加外键约束（如果不需要外键约束可去掉）
                           FOREIGN KEY (kid_id) REFERENCES kid(id)
);

CREATE TABLE right_hand (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            kid_id BIGINT,
                            flexibility VARCHAR(255), -- 对应灵活性
                            joint_swelling VARCHAR(255), -- 对应指关节肿胀
                            two_point_discrimination VARCHAR(255), -- 对应两点辨别觉
                            nail_bed_circulation VARCHAR(255), -- 对应甲床循环
                            recommendation TEXT, -- 存储推荐字符串
                            create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    -- 假设 kid 表的主键是 id，这里添加外键约束（如果不需要外键约束可去掉）
                            FOREIGN KEY (kid_id) REFERENCES kid(id)
);

CREATE TABLE right_foot (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            kid_id BIGINT,
                            arch_status VARCHAR(255),
                            hallux_valgus_degree VARCHAR(255),
                            callus_status VARCHAR(255),
                            gait_cycle_status VARCHAR(255),
                            recommendation TEXT,
                            create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            FOREIGN KEY (kid_id) REFERENCES kid(id)
);

CREATE TABLE left_foot (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           kid_id BIGINT,
                           arch_status VARCHAR(255),
                           hallux_valgus_degree VARCHAR(255),
                           callus_status VARCHAR(255),
                           gait_cycle_status VARCHAR(255),
                           recommendation TEXT,
                           create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (kid_id) REFERENCES kid(id)
);

CREATE TABLE body (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      kid_id BIGINT,
                      scoliosis_degree VARCHAR(255),
                      core_strength VARCHAR(255),
                      body_fat_percentage VARCHAR(255),
                      flexibility VARCHAR(255),
                      recommendation TEXT,
                      create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      FOREIGN KEY (kid_id) REFERENCES kid(id)
);


CREATE TABLE left_arm (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          kid_id BIGINT,
                          grip_strength VARCHAR(255),
                          elbow_range_of_motion VARCHAR(255),
                          tinel_sign VARCHAR(255),
                          circumference_difference VARCHAR(255),
                          recommendation TEXT,
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (kid_id) REFERENCES kid(id)
);


CREATE TABLE right_arm (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           kid_id BIGINT,
                           grip_strength VARCHAR(255),
                           elbow_range_of_motion VARCHAR(255),
                           tinel_sign VARCHAR(255),
                           circumference_difference VARCHAR(255),
                           recommendation TEXT,
                           create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (kid_id) REFERENCES kid(id)
);

CREATE TABLE head (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      kid_id BIGINT,
                      headache_frequency VARCHAR(255),
                      dizziness VARCHAR(255),
                      trauma_history VARCHAR(255),
                      cognitive_test_result VARCHAR(255),
                      recommendation TEXT,
                      create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      FOREIGN KEY (kid_id) REFERENCES kid(id)
);

CREATE TABLE left_leg (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          kid_id BIGINT,
                          length_difference VARCHAR(255),
                          muscle_strength VARCHAR(255),
                          knee_reflex VARCHAR(255),
                          swelling_degree VARCHAR(255),
                          recommendation TEXT,
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (kid_id) REFERENCES kid(id)
);

CREATE TABLE right_leg (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           kid_id BIGINT,
                           length_difference VARCHAR(255),
                           muscle_strength VARCHAR(255),
                           knee_reflex VARCHAR(255),
                           swelling_degree VARCHAR(255),
                           recommendation TEXT,
                           create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (kid_id) REFERENCES kid(id)
);

CREATE TABLE left_shoulder (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               kid_id BIGINT,
                               range_of_motion VARCHAR(255),
                               pain_index VARCHAR(255),
                               stability VARCHAR(255),
                               muscle_strength VARCHAR(255),
                               recommendation TEXT,
                               create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               FOREIGN KEY (kid_id) REFERENCES kid(id)
);

CREATE TABLE right_shoulder (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                kid_id BIGINT,
                                range_of_motion VARCHAR(255),
                                pain_index VARCHAR(255),
                                stability VARCHAR(255),
                                muscle_strength VARCHAR(255),
                                recommendation TEXT,
                                create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                FOREIGN KEY (kid_id) REFERENCES kid(id)
);

CREATE TABLE visual (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        kid_id BIGINT,
                        left_vision VARCHAR(255),
                        right_vision VARCHAR(255),
                        left_astigmatism VARCHAR(255),
                        right_astigmatism VARCHAR(255),
                        color_vision VARCHAR(255),
                        recommendation TEXT,
                        create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (kid_id) REFERENCES kid(id)
);

CREATE TABLE oral (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      kid_id BIGINT,
                      deciduous_teeth VARCHAR(255),
                      permanent_teeth VARCHAR(255),
                      decayed_teeth VARCHAR(255),
                      gum_condition VARCHAR(255),
                      occlusion VARCHAR(255),
                      recommendation TEXT,
                      create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      FOREIGN KEY (kid_id) REFERENCES kid(id)
);

CREATE TABLE endocrine (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           kid_id BIGINT,
                           thyroid_function VARCHAR(255),
                           growth_hormone VARCHAR(255),
                           insulin VARCHAR(255),
                           metabolic_rate VARCHAR(255),
                           recommendation TEXT,
                           create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (kid_id) REFERENCES kid(id)
);

CREATE TABLE ent (
                     id INT AUTO_INCREMENT PRIMARY KEY,
                     kid_id BIGINT,
                     left_hearing VARCHAR(255),
                     right_hearing VARCHAR(255),
                     sinus_condition VARCHAR(255),
                     tonsil_condition VARCHAR(255),
                     throat_condition VARCHAR(255),
                     recommendation TEXT,
                     create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                     update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                     FOREIGN KEY (kid_id) REFERENCES kid(id)
);

CREATE TABLE respiratory (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             kid_id BIGINT,
                             vital_capacity VARCHAR(255),
                             respiratory_frequency VARCHAR(255),
                             lung_adventitious_sound VARCHAR(255),
                             airway_patency VARCHAR(255),
                             recommendation TEXT,
                             create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             FOREIGN KEY (kid_id) REFERENCES kid(id)
);