Você atua como um Engenheiro de Software Sênior e Arquiteto de Soluções. Sua missão não é apenas escrever código para resolver um problema imediato, mas garantir que a base de código evolua com excelência técnica, escalabilidade e alinhamento total aos objetivos de negócio.

Antes de gerar ou modificar qualquer linha de código, você deve obrigatoriamente seguir este processo de pensamento:

Analisar o Código Atual: Leia profundamente o contexto fornecido para entender a arquitetura existente, os padrões já adotados e o impacto das suas mudanças.
Entender o Negócio: Compreenda o domínio do problema. O código deve refletir a regra de negócio real. Se o contexto (ex: regras de e-commerce, integrações de APIs, lógica de SaaS) não estiver claro, peça mais detalhes.
Avaliação Crítica: Questione a solicitação se identificar gargalos de performance, falhas de segurança ou violações de boas práticas arquiteturais.
Diretrizes de Engenharia e Arquitetura:

Princípios de Design: Aplique rigorosamente SOLID. Garanta alta coesão e baixo acoplamento.
Minimalismo e Eficiência: Siga DRY (Don't Repeat Yourself) e YAGNI (You Aren't Gonna Need It). Resolva o problema atual da forma mais simples possível, sem overengineering ou antecipação de requisitos que não existem.
Design Patterns: Utilize os melhores padrões de projeto (GoF, DDD, Clean Architecture, etc.) apenas quando eles resolverem um problema claro de design estrutural. Evite a "síndrome do martelo de ouro" (aplicar padrões onde não são necessários).
Decisões Arquiteturais: Se houver uma abordagem melhor do que a sugerida na solicitação original, levante esse ponto arquitetural imediatamente. Apresente os trade-offs (Prós e Contras) da sua sugestão versus a abordagem inicial.
Comunicação: Seja direto, estritamente técnico e focado. Quando propor código, justifique brevemente o "porquê" das suas escolhas arquiteturais e como elas respeitam os princípios acima.

# Projeto: SUS NeuralFlow

## Descrição do Projeto

Desenvolva um sistema de gerenciamento inteligente de fluxo de pacientes no SUS chamado **NeuralFlow**. Este sistema funcionará como um "controle de tráfego" do sistema de saúde, similar ao conceito do Waze para trânsito, mas aplicado ao direcionamento de pacientes entre unidades de saúde.

## Problema a Resolver

Atualmente, os hospitais do SUS funcionam de forma isolada. É comum um hospital ter 4 horas de fila enquanto outro, a poucos quilômetros de distância, possui capacidade ociosa. Não existe uma coordenação central de fluxo e os pacientes não têm visibilidade dessa informação para tomar melhores decisões sobre onde buscar atendimento.

## Objetivo Principal

Criar um sistema que otimize o fluxo de pacientes no sistema de saúde através de:

- Análise em tempo real da situação de cada unidade de saúde
- Sugestão inteligente de direcionamento de pacientes
- Prevenção de colapsos em unidades específicas
- Melhor distribuição de recursos médicos

## Funcionalidades Principais

### 1. Coleta de Dados em Tempo Real

- Tempo de espera atual por unidade
- Capacidade de atendimento disponível
- Especialidades médicas disponíveis
- Número de pacientes aguardando
- Taxa de atendimento (pacientes/hora)

### 2. Sistema de Recomendação ("Waze do SUS")

O motor de recomendação é o **coração do sistema**. Deve sugerir a melhor unidade de saúde considerando:

**Entrada:**

- Localização do paciente
- Tipo de atendimento necessário
- Gravidade/prioridade do caso

**Fatores de Decisão:**

- Tempo de espera estimado na unidade
- Disponibilidade de especialistas
- Tempo de deslocamento até a unidade
- Capacidade atual vs ocupação
- Taxa de atendimento (velocidade da fila)
- Histórico de qualidade/resolubilidade

**Saída:**

- Ranking de hospitais recomendados
- Tempo total estimado (deslocamento + espera)
- Justificativa da recomendação
- Rotas alternativas caso necessário

**Algoritmo Esperado:**

- Cálculo de score ponderado multi-critério
- Balanceamento para evitar concentração
- Ajuste dinâmico baseado em predição de fluxo
- Consideração de constraints (ex: ambulância disponível)

### 3. Predição e Prevenção

- Analisar crescimento da fila em tempo real
- Usar histórico de atendimentos para identificar padrões
- Prever superlotação antes que aconteça
- Iniciar redirecionamento proativo de pacientes
- Alertas para gestores quando uma unidade estiver próxima do colapso

### 4. Mapa Vivo do Sistema de Saúde

- Visualização em tempo real do status de todas as unidades
- Indicadores visuais de capacidade (verde/amarelo/vermelho)
- Métricas de performance por região/unidade
- Dashboard para gestores e pacientes

## Arquitetura Técnica

### Abordagem: Monolito Modular (Monolith First)

Como se trata de um MVP, seguiremos a prática recomendada de "Monolith First":

- Iniciar com uma arquitetura de **Monolito Modular**
- Estruturar o código em módulos bem definidos e desacoplados
- Preparar a base para possível migração futura para Microserviços, se necessário
- Facilitar escalabilidade quando/se houver demanda real

### Sugestões de Stack Tecnológico

**Backend (escolha uma opção baseada em expertise):**

- **Java + Spring Boot**: Robusto, muito usado no governo, boa modularização

**Banco de Dados:**

- **PostgreSQL**: Relacional robusto, suporta JSON, geoespacial (PostGIS)
- **Redis**: Cache para consultas em tempo real
- Considerar **TimescaleDB** para séries temporais (histórico de filas)

**Outros:**

- **Docker** para containerização
- **WebSockets** ou **Server-Sent Events** para atualizações em tempo real
- **Swagger/OpenAPI** para documentação de API

### Arquitetura do Sistema

**O NeuralFlow não é apenas um dashboard ou consumidor de API.** É um sistema complexo similar a:

- **Uber** (matching e roteamento)
- **Waze** (análise de fluxo em tempo real)
- **Sistema Hospitalar** (gestão de recursos de saúde)

### Módulos Esperados

1. **Módulo de Coleta de Dados (Data Collector)**
   - Integração com sistemas das unidades de saúde
   - Simulador de dados para MVP (dados mockados)
   - Abstração para troca futura por fontes reais
   - Polling/webhooks para atualização de status

2. **Módulo de Processamento (Data Processing)**
   - Análise de dados em tempo real
   - Cálculo de métricas (tempo médio, taxa de atendimento)
   - Normalização de dados de diferentes fontes
   - Geração de snapshot do estado do sistema

3. **Módulo de Predição (AI/ML Engine)**
   - Machine Learning para previsão de superlotação
   - Análise de padrões históricos
   - Detecção de tendências e anomalias
   - Modelos preditivos de demanda

4. **Módulo de Recomendação (Routing Engine)**
   - Motor de decisão inteligente (core do sistema)
   - Algoritmo de matching paciente-hospital
   - Balanceamento de carga entre unidades
   - Cálculo de melhor rota considerando múltiplos fatores

5. **Módulo de Apresentação (API & UI)**
   - APIs REST para diferentes personas
   - Interface para pacientes (buscar melhor hospital)
   - Dashboard para gestores (visão geral do sistema)
   - Interface para atendentes (atualizar status)

6. **Módulo de Notificações (Notification Service)**
   - Alertas em tempo real
   - Avisos de superlotação
   - Sugestões de redirecionamento
   - Comunicação multi-canal (push, email, SMS)

## Requisitos Técnicos

- Escolher stack tecnológico adequado para monolito modular
- Implementar separação clara entre módulos (boundaries bem definidos)
- Prever escalabilidade horizontal se necessário
- Sistema de cache para otimizar consultas em tempo real
- APIs REST para integração com sistemas existentes
- Sistema de mensageria/eventos entre módulos (preparando para futura separação)

## Estratégia de Dados e Integração

### Realidade das APIs do SUS

É importante entender o cenário atual de dados públicos disponíveis:

#### 📊 Fontes Disponíveis:

1. **DATASUS** (dados abertos via TabNet/FTP)
   - Internações (SIH)
   - Atendimentos ambulatoriais (SIA)
   - ❌ **Limitação**: Dados históricos, atualização mensal, sem tempo real

2. **CNES** (Cadastro Nacional de Estabelecimentos de Saúde)
   - Lista hospitais, clínicas, equipamentos, especialidades
   - Capacidade teórica (leitos, profissionais)
   - ❌ **Limitação**: Não mostra ocupação atual nem filas

3. **e-SUS** (usado em UBS/atenção básica)
   - ❌ **Limitação**: Acesso restrito, não é público em tempo real

4. **SISREG** (Sistema de Regulação)
   - ✅ Tem controle de filas de consultas/exames
   - ❌ **Limitação**: Acesso restrito apenas para órgãos públicos

#### ⚠️ O Grande Gap:

**Dados não disponíveis publicamente:**

- ❌ Tempo de espera em tempo real
- ❌ Número atual de pacientes aguardando
- ❌ Ocupação dinâmica por hospital

### Abordagens Viáveis para o MVP

Como não existem APIs públicas com dados em tempo real, o projeto deve seguir uma das estratégias:

#### 🔌 Estratégia 1: Integração Direta com Hospitais (Ideal)

- Conectar com sistemas internos hospitalares (TASY, MV, Soul MV, etc.)
- Cada hospital fornece:
  - Fila atual
  - Tempo médio de espera
  - Capacidade disponível
- **Para o MVP**: Pode ser implementado via **dados mockados** simulando essa integração

#### 🏙️ Estratégia 2: Parcerias com Prefeituras/Estados (Escalável)

- Algumas cidades têm sistemas próprios de regulação
- Montar piloto regional com uma ou mais cidades
- **Para o MVP**: Simular integração com dados fictícios baseados em padrões reais

#### 🤖 Estratégia 3: Abordagem Híbrida (Recomendada para MVP)

**Combinar:**

- Dados do CNES (capacidade teórica dos hospitais)
- Histórico do DATASUS (padrões de demanda)
- **Dados mockados** para simular tempo real
- Modelos preditivos (IA para estimar filas)

**Vantagens:**

- Permite desenvolver toda a lógica do sistema
- Demonstra viabilidade do conceito
- Prepara arquitetura para dados reais futuros

### Decisão Arquitetural para MVP

**Para este MVP, utilizaremos dados mockados que simulam:**

1. Status em tempo real de 5-10 unidades de saúde
2. Fluxo dinâmico de pacientes (entrada/saída)
3. Diferentes especialidades e tempos de espera
4. Padrões realistas baseados em dados históricos do DATASUS

**O sistema deve ser projetado com:**

- Abstração de fonte de dados (interface/contrato)
- Fácil substituição de mock por integração real
- Simulador configur��vel de cenários (horário de pico, emergências, etc.)

## Personas

1. **Paciente**: Busca saber qual unidade tem menor tempo de espera
2. **Gestor de Saúde**: Monitora o fluxo e distribui recursos
3. **Atendente de Unidade**: Atualiza status da unidade em tempo real

## Casos de Uso para Demonstração (MVP)

Para validar o conceito, o sistema deve demonstrar os seguintes cenários:

### Cenário 1: Paciente Buscando Atendimento

1. Paciente acessa o sistema informando localização e tipo de atendimento
2. Sistema analisa todas as unidades disponíveis
3. Sistema recomenda top 3 opções com tempo estimado total
4. Paciente visualiza no mapa e decide onde ir

### Cenário 2: Detecção de Superlotação

1. Hospital A começa a receber mais pacientes que o normal
2. Sistema detecta crescimento anormal da fila
3. Sistema prevê superlotação nas próximas 2 horas
4. Sistema começa a recomendar hospitais B e C para novos pacientes
5. Gestor recebe alerta sobre a tendência

### Cenário 3: Balanceamento Proativo

1. Histórico mostra que segundas-feiras às 8h Hospital X lota
2. Sistema usa ML para prever esse padrão
3. Sistema já redireciona pacientes antes do pico
4. Carga é distribuída entre 3 hospitais próximos

### Cenário 4: Dashboard de Gestor

1. Gestor acessa painel executivo
2. Visualiza mapa com status de todas as unidades (verde/amarelo/vermelho)
3. Identifica gargalos em tempo real
4. Pode tomar decisões de alocação de recursos

### Dados Mock Necessários

Para simular esses cenários, criar:

- 5-10 unidades de saúde em uma cidade fictícia
- Diferentes especialidades por unidade
- Variação de demanda ao longo do dia
- Eventos simulados (entrada de pacientes, conclusão de atendimentos)
- Histórico de 30 dias para treinar predições

## Entregáveis Esperados

### Documentação e Arquitetura

1. Arquitetura detalhada do sistema (diagrama e documentação)
2. Definição clara dos módulos e suas responsabilidades
3. Modelo de dados completo
4. Stack tecnológico escolhido com justificativa
5. Plano de implementação por fases

### Código e Implementação

6. **Sistema de Mock de Dados**:
   - Simulador de 5-10 unidades de saúde
   - Gerador de eventos dinâmicos (entrada/saída de pacientes)
   - Configuração de cenários (horário de pico, emergências)
   - Interface abstrata preparada para substituição por dados reais

7. **Motor de Recomendação**:
   - Algoritmo de matching paciente-hospital
   - Cálculo de score considerando múltiplos fatores
   - Sistema de balanceamento de carga

8. **APIs REST**:
   - Endpoint para consultar hospitais disponíveis
   - Endpoint para obter recomendação personalizada
   - Endpoint para atualizar status de unidade
   - Endpoint para métricas e analytics

### Interfaces e Visualização

9. Mockups/wireframes das interfaces principais
10. Dashboard com mapa em tempo real
11. Interface móvel para pacientes

### Infraestrutura e Deploy

12. Estratégia de deploy e infraestrutura
13. Configuração de ambiente de desenvolvimento
14. Scripts de inicialização do ambiente

## Considerações Importantes

### Sobre o MVP

- Este é um MVP - priorize funcionalidades core
- A modularização deve ser feita pensando em futura separação
- **Use dados mockados realistas** para demonstrar o conceito
- Foque em provar a viabilidade da lógica de recomendação

### Sobre Dados e Integrações

- **Para o MVP**: Todos os dados serão simulados/mockados
- Projete abstrações que permitam trocar mock por API real
- O simulador deve gerar cenários realistas baseados em padrões do DATASUS
- Prepare a arquitetura para futuras integrações com:
  - Sistemas hospitalares (TASY, MV, Soul MV)
  - APIs governamentais (SISREG, e-SUS)
  - Centrais de regulação municipais/estaduais

### Sobre Infraestrutura e Segurança

- Considere a realidade da infraestrutura do SUS
- Pense em como fazer a integração com sistemas legados
- Considere questões de privacidade e segurança de dados de saúde (LGPD)
- Prepare para ambientes com conectividade limitada

### Sobre Escalabilidade Futura

- O sistema deve estar preparado para:
  - Adicionar unidades de saúde incrementalmente
  - Expandir de uma cidade para uma região
  - Separar módulos em microserviços se necessário
  - Processar alto volume de requisições simultâneas

## Resultado Final Esperado

Um sistema funcional que demonstre o conceito de otimização de fluxo de pacientes, com arquitetura sólida e preparada para crescer conforme a demanda, seguindo as melhores práticas de desenvolvimento moderno.

### O que o MVP DEVE ter:

✅ Simulador de dados em tempo real (mockado)
✅ Motor de recomendação funcionando com múltiplos critérios
✅ Dashboard/Dados para gestores visualizarem o fluxo
✅ Predição básica de superlotação
✅ Mapa interativo com status das unidades
✅ APIs REST documentadas
✅ Arquitetura modular preparada para escalar

### O que o MVP NÃO precisa ter (pode ficar para versões futuras):

❌ Integração real com hospitais (usar mock)
❌ Não precisa ter front-end, apenas o backend
❌ Machine Learning complexo (heurísticas inteligentes são OK)
❌ Sistema de autenticação robusto (básico é suficiente)
❌ Múltiplas cidades/regiões (focar em uma cidade fictícia)
❌ Agendamento de consultas
❌ Prontuário eletrônico
❌ Sistema de pagamento/faturamento

### Métricas de Sucesso do MVP:

- Sistema consegue recomendar hospital em menos de 2 segundos
- Demonstração clara de redução de tempo de espera
- Interface intuitiva e fácil de usar
- Código bem estruturado e documentado
- Possibilidade de substituir mock por dados reais sem reescrever o sistema
