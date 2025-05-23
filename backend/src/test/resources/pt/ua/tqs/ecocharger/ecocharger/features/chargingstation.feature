@ET-18
Feature: Home Page

  Scenario: Ver estações no mapa após login
    Given o utilizador está autenticado
    When o utilizador abre a página inicial
    Then o mapa deve mostrar múltiplas estações
    And deve ver a mensagem "Selecione uma estação no mapa"

  Scenario: Ver detalhes de uma estação ao clicar num marcador
    Given o utilizador está na página inicial com estações carregadas
    When o utilizador clica num marcador de estação no mapa
    Then os detalhes da estação devem aparecer no painel lateral
